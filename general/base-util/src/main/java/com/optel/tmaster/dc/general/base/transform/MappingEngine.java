/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.base.transform;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reflection-based mapping engine for YANG-generated beans.
 *
 * <p>Auto-copies same-name fields from a source data object to a target builder,
 * supports explicit field name mappings, and registered type converters.</p>
 *
 * <h3>Usage</h3>
 * <pre>{@code
 * // 1. Auto-copy all same-name fields
 * TargetBuilder builder = new TargetBuilder();
 * MappingEngine.copy(source, builder);
 *
 * // 2. Auto-copy with explicit renames
 * MappingEngine.copy(source, builder,
 *     FieldMapping.of("adminStatus", "adminState"),
 *     FieldMapping.of("protType", "protectionType"));
 *
 * // 3. Register enum converters once at startup
 * MappingEngine.registerConverter(ApiProtectionType.class, DevProtectionType.class,
 *     api -> DevProtectionType.valueOf(api.name()));
 * }</pre>
 */
public final class MappingEngine {

    private static final Logger LOG = LoggerFactory.getLogger(MappingEngine.class);

    // Methods to skip during auto-mapping
    private static final Set<String> SKIP_METHODS = Set.of(
        "getClass", "getImplementedInterface", "getYangMode", "getNeId", "getValue"
    );

    // Cache of getter/setter metadata per class
    private static final Map<Class<?>, Map<String, Method>> GETTER_CACHE = new ConcurrentHashMap<>();
    private static final Map<Class<?>, Map<String, Method>> SETTER_CACHE = new ConcurrentHashMap<>();

    // Registered type converters: sourceType → targetType → converter
    private static final Map<Class<?>, Map<Class<?>, Function<Object, Object>>> CONVERTERS =
        new ConcurrentHashMap<>();

    // Feature flags
    private static boolean nestedMappingEnabled = true;
    private static boolean withKeyDetectionEnabled = true;

    // Cache of build() methods per builder class
    private static final Map<Class<?>, Method> BUILD_METHOD_CACHE = new ConcurrentHashMap<>();

    // Cache of withKey() methods per builder class (null → no withKey)
    private static final Map<Class<?>, Method> WITH_KEY_CACHE = new ConcurrentHashMap<>();

    private MappingEngine() { }

    // ─── Public API ────────────────────────────────────────────

    /**
     * Auto-copy all same-name same-type fields from source to target builder.
     *
     * @return the target builder (for fluent chaining)
     */
    public static <T> T copy(Object source, T target) {
        return copyInternal(source, target, Collections.emptyMap(), Collections.emptySet());
    }

    /**
     * Auto-copy, with explicit field name mappings for renamed fields.
     */
    public static <T> T copy(Object source, T target, FieldMapping... mappings) {
        Map<String, FieldMapping> mappingMap = new HashMap<>();
        Set<String> mappedTargetFields = new HashSet<>();
        for (FieldMapping m : mappings) {
            mappingMap.put(m.sourceField(), m);
            mappedTargetFields.add(m.targetField());
        }
        return copyInternal(source, target, mappingMap, mappedTargetFields);
    }

    /**
     * Auto-copy, skipping the specified target field names.
     */
    public static <T> T copyExcluding(Object source, T target, String... excludeFields) {
        return copyInternal(source, target, Collections.emptyMap(), new HashSet<>(Arrays.asList(excludeFields)));
    }

    /**
     * Register a type converter for auto-mapping. When the engine encounters
     * source type S → target param type T, it uses this converter.
     */
    @SuppressWarnings("unchecked")
    public static <S, T> void registerConverter(Class<S> sourceType, Class<T> targetType,
                                                 Function<S, T> converter) {
        CONVERTERS
            .computeIfAbsent(sourceType, k -> new ConcurrentHashMap<>())
            .put(targetType, (Function<Object, Object>) converter);
        LOG.debug("Registered converter: {} → {}", sourceType.getSimpleName(), targetType.getSimpleName());
    }

    /**
     * Register enum-to-enum converter by matching constant names.
     * Useful when API and device enums share the same names but are different Java types.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static void registerEnumConverter(Class sourceType, Class targetType) {
        Map<String, Enum> nameMap = new HashMap<>();
        for (Object c : targetType.getEnumConstants()) {
            nameMap.put(((Enum<?>) c).name(), (Enum) c);
        }
        registerConverter(sourceType, targetType, s -> nameMap.get(((Enum<?>) s).name()));
    }

    /**
     * Enable or disable nested recursive mapping. Default is true.
     *
     * <p>When enabled, if a source field type doesn't match the target setter parameter type
     * and no converter is registered, the engine will try to find a Builder for the target
     * type and recursively copy fields.</p>
     */
    public static void setNestedMappingEnabled(boolean enabled) {
        nestedMappingEnabled = enabled;
    }

    public static boolean isNestedMappingEnabled() {
        return nestedMappingEnabled;
    }

    /**
     * Enable or disable automatic withKey detection. Default is true.
     *
     * <p>When enabled, after copying fields to a builder, the engine checks if the builder
     * has a {@code withKey(XxxKey)} method and auto-constructs the key from matching
     * source/builder fields.</p>
     */
    public static void setWithKeyDetectionEnabled(boolean enabled) {
        withKeyDetectionEnabled = enabled;
    }

    public static boolean isWithKeyDetectionEnabled() {
        return withKeyDetectionEnabled;
    }

    /**
     * Clear all registered converters and caches.
     */
    public static void reset() {
        CONVERTERS.clear();
        GETTER_CACHE.clear();
        SETTER_CACHE.clear();
        BUILD_METHOD_CACHE.clear();
        WITH_KEY_CACHE.clear();
    }

    // ─── Internal ──────────────────────────────────────────────

    @SuppressWarnings("unchecked")
    private static <T> T copyInternal(Object source, T target,
                                       Map<String, FieldMapping> explicitMappings,
                                       Set<String> skipTargetFields) {
        if (source == null || target == null) {
            return target;
        }

        Map<String, Method> sourceGetters = getGetters(source.getClass());
        Map<String, Method> targetSetters = getSetters(target.getClass());

        for (var getterEntry : sourceGetters.entrySet()) {
            String sourceField = getterEntry.getKey();
            Method getter = getterEntry.getValue();

            FieldMapping explicit = explicitMappings.get(sourceField);
            String targetField = explicit != null ? explicit.targetField() : sourceField;

            // Only skip auto-mapped fields that conflict with an explicit target mapping
            if (explicit == null && skipTargetFields.contains(targetField)) {
                continue;
            }

            Method setter = targetSetters.get(targetField);
            if (setter == null) {
                continue;
            }

            Object value;
            try {
                value = getter.invoke(source);
            } catch (IllegalAccessException | InvocationTargetException e) {
                LOG.trace("Failed to read {}: {}", sourceField, e.getMessage());
                continue;
            }

            if (value == null) {
                continue;
            }

            Class<?> paramType = setter.getParameterTypes()[0];

            // If explicit mapping has a converter, use it
            if (explicit != null && explicit.converter() != null) {
                try {
                    Object converted = explicit.converter().convert(value);
                    if (converted != null) {
                        setter.invoke(target, converted);
                    }
                } catch (Exception e) {
                    LOG.trace("Converter failed for {}: {}", sourceField, e.getMessage());
                }
                continue;
            }

            // Direct type match
            if (paramType.isAssignableFrom(value.getClass())) {
                try {
                    setter.invoke(target, value);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    LOG.trace("Failed to set {}: {}", targetField, e.getMessage());
                }
                continue;
            }

            // Try registered converter
            Function<Object, Object> converter = findConverter(value.getClass(), paramType);
            if (converter != null) {
                try {
                    Object converted = converter.apply(value);
                    if (converted != null) {
                        setter.invoke(target, converted);
                    }
                } catch (Exception e) {
                    LOG.trace("Registered converter failed for {}: {}", sourceField, e.getMessage());
                }
                continue;
            }

            // Try nested recursive mapping
            if (nestedMappingEnabled) {
                Object nestedResult = mapNested(value, paramType);
                if (nestedResult != null) {
                    try {
                        setter.invoke(target, nestedResult);
                    } catch (Exception e) {
                        LOG.trace("Failed to set nested {}: {}", targetField, e.getMessage());
                    }
                }
            }
        }

        // Post-processing: auto-detect withKey
        if (withKeyDetectionEnabled) {
            autoDetectWithKey(source, target, sourceGetters);
        }

        return target;
    }

    // ─── Nested recursive mapping ──────────────────────────────

    /**
     * Map a source object to an instance of targetType by finding the Builder
     * (by naming convention: TargetType + "Builder"), recursively copying fields,
     * and calling build().
     */
    private static Object mapNested(Object source, Class<?> targetType) {
        // Try builder approach: TargetType → TargetTypeBuilder
        String builderClassName = targetType.getName() + "Builder";
        try {
            Class<?> builderClass = Class.forName(builderClassName);
            Object builder = builderClass.getDeclaredConstructor().newInstance();
            copy(source, builder);
            return invokeBuild(builder);
        } catch (ClassNotFoundException e) {
            // No builder — try direct construction
            return constructDirectly(source, targetType);
        } catch (NoSuchMethodException e) {
            LOG.trace("No zero-arg constructor for builder {}", builderClassName);
            return null;
        } catch (Exception e) {
            LOG.trace("Nested mapping failed for {}: {}", targetType.getSimpleName(), e.getMessage());
            return null;
        }
    }

    // ─── Direct construction (constructor parameter matching) ──

    /**
     * Construct targetType directly by matching constructor parameter names
     * to source field names.
     */
    private static Object constructDirectly(Object source, Class<?> targetType) {
        Map<String, Method> sourceGetters = getGetters(source.getClass());
        Constructor<?> bestCtor = findBestConstructor(targetType, sourceGetters);
        if (bestCtor == null) {
            return null;
        }
        Object[] args = resolveConstructorArgs(bestCtor, sourceGetters, source);
        if (args == null) {
            return null;
        }
        try {
            return bestCtor.newInstance(args);
        } catch (Exception e) {
            LOG.trace("Constructor invocation failed for {}: {}", targetType.getSimpleName(), e.getMessage());
            return null;
        }
    }

    /**
     * Find the constructor whose parameters best match source field names.
     * Tries name-based matching first (requires -parameters), then falls back
     * to type-based matching for single-arg constructors.
     */
    private static Constructor<?> findBestConstructor(Class<?> type,
                                                      Map<String, Method> sourceGetters) {
        Constructor<?>[] ctors = type.getConstructors();
        // First try: parameter name matching (requires -parameters compiler flag)
        Constructor<?> best = null;
        int bestScore = 0;
        for (Constructor<?> ctor : ctors) {
            if (ctor.getParameterCount() == 0) {
                continue;
            }
            int score = 0;
            for (Parameter param : ctor.getParameters()) {
                if (param.isNamePresent() && sourceGetters.containsKey(param.getName())) {
                    score++;
                }
            }
            if (score > bestScore && score == ctor.getParameterCount()) {
                best = ctor;
                bestScore = score;
            }
        }
        if (best != null) {
            return best;
        }
        // Fallback: single-arg constructors matched by type
        for (Constructor<?> ctor : ctors) {
            if (ctor.getParameterCount() != 1) {
                continue;
            }
            Class<?> paramType = ctor.getParameterTypes()[0];
            for (Method getter : sourceGetters.values()) {
                if (paramType.isAssignableFrom(getter.getReturnType())) {
                    return ctor;
                }
            }
        }
        return null;
    }

    /**
     * Resolve constructor arguments from source getters by matching parameter names
     * or types (fallback when -parameters is not available).
     * Returns null if any parameter cannot be resolved.
     */
    private static Object[] resolveConstructorArgs(Constructor<?> ctor,
                                                    Map<String, Method> sourceGetters,
                                                    Object source) {
        Parameter[] params = ctor.getParameters();
        if (params.length == 0) {
            return null;
        }
        // Name-based matching (requires -parameters)
        if (params[0].isNamePresent()) {
            return resolveByName(params, sourceGetters, source);
        }
        // Fallback: single-arg type matching
        if (params.length == 1) {
            return resolveByType(params[0], sourceGetters, source);
        }
        return null;
    }

    private static Object[] resolveByName(Parameter[] params,
                                           Map<String, Method> sourceGetters,
                                           Object source) {
        Object[] args = new Object[params.length];
        for (int i = 0; i < params.length; i++) {
            if (!params[i].isNamePresent()) {
                return null;
            }
            Method getter = sourceGetters.get(params[i].getName());
            if (getter == null) {
                return null;
            }
            Object value;
            try {
                value = getter.invoke(source);
            } catch (Exception e) {
                return null;
            }
            if (value == null) {
                return null;
            }
            if (!params[i].getType().isAssignableFrom(value.getClass())) {
                return null;
            }
            args[i] = value;
        }
        return args;
    }

    private static Object[] resolveByType(Parameter param,
                                           Map<String, Method> sourceGetters,
                                           Object source) {
        Class<?> paramType = param.getType();
        for (Method getter : sourceGetters.values()) {
            if (paramType.isAssignableFrom(getter.getReturnType())) {
                Object value;
                try {
                    value = getter.invoke(source);
                } catch (Exception e) {
                    continue;
                }
                if (value != null) {
                    return new Object[] { value };
                }
            }
        }
        return null;
    }

    // ─── withKey auto-detection ─────────────────────────────────

    private static void autoDetectWithKey(Object source, Object builder,
                                           Map<String, Method> sourceGetters) {
        Method withKey = findWithKey(builder.getClass());
        if (withKey == null) {
            return;
        }
        // Check if key is already set
        if (isKeyAlreadySet(builder)) {
            return;
        }
        Class<?> keyType = withKey.getParameterTypes()[0];
        Object key = constructKeyFromFields(keyType, source, builder, sourceGetters);
        if (key != null) {
            try {
                withKey.invoke(builder, key);
            } catch (Exception e) {
                LOG.trace("withKey invocation failed: {}", e.getMessage());
            }
        }
    }

    private static Method findWithKey(Class<?> builderClass) {
        return WITH_KEY_CACHE.computeIfAbsent(builderClass, cls -> {
            for (Method m : cls.getMethods()) {
                if ("withKey".equals(m.getName())
                        && m.getParameterCount() == 1
                        && !m.getDeclaringClass().equals(Object.class)) {
                    return m;
                }
            }
            return null;
        });
    }

    private static boolean isKeyAlreadySet(Object builder) {
        try {
            Method keyGetter = builder.getClass().getMethod("key");
            return keyGetter.invoke(builder) != null;
        } catch (NoSuchMethodException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Try to construct a Key from source fields (preferred) or builder fields.
     */
    private static Object constructKeyFromFields(Class<?> keyType, Object source,
                                                  Object builder,
                                                  Map<String, Method> sourceGetters) {
        // Try source fields first
        Object key = tryConstruct(keyType, sourceGetters, source);
        if (key != null) {
            return key;
        }
        // Try builder fields
        Map<String, Method> builderGetters = getGetters(builder.getClass());
        return tryConstruct(keyType, builderGetters, builder);
    }

    private static Object tryConstruct(Class<?> keyType, Map<String, Method> fieldGetters,
                                        Object fieldSource) {
        Constructor<?> bestCtor = findBestConstructor(keyType, fieldGetters);
        if (bestCtor == null) {
            return null;
        }
        Object[] args = resolveConstructorArgs(bestCtor, fieldGetters, fieldSource);
        if (args == null) {
            return null;
        }
        try {
            return bestCtor.newInstance(args);
        } catch (Exception e) {
            LOG.trace("Key construction failed: {}", e.getMessage());
            return null;
        }
    }

    // ─── Build invocation ──────────────────────────────────────

    private static Object invokeBuild(Object builder) {
        Method buildMethod = BUILD_METHOD_CACHE.computeIfAbsent(builder.getClass(), cls -> {
            try {
                return cls.getMethod("build");
            } catch (NoSuchMethodException e) {
                return null;
            }
        });
        if (buildMethod == null) {
            return null;
        }
        try {
            return buildMethod.invoke(builder);
        } catch (Exception e) {
            LOG.trace("build() invocation failed: {}", e.getMessage());
            return null;
        }
    }

    private static Function<Object, Object> findConverter(Class<?> sourceType, Class<?> targetType) {
        // Direct match
        Map<Class<?>, Function<Object, Object>> bySource = CONVERTERS.get(sourceType);
        if (bySource != null) {
            Function<Object, Object> c = bySource.get(targetType);
            if (c != null) return c;
        }
        // Walk source supertypes
        for (var entry : CONVERTERS.entrySet()) {
            if (entry.getKey().isAssignableFrom(sourceType)) {
                for (var e : entry.getValue().entrySet()) {
                    if (targetType.isAssignableFrom(e.getKey())) {
                        return e.getValue();
                    }
                }
            }
        }
        return null;
    }

    // ─── Reflection helpers ────────────────────────────────────

    private static Map<String, Method> getGetters(Class<?> clazz) {
        return GETTER_CACHE.computeIfAbsent(clazz, MappingEngine::scanGetters);
    }

    private static Map<String, Method> getSetters(Class<?> clazz) {
        return SETTER_CACHE.computeIfAbsent(clazz, MappingEngine::scanSetters);
    }

    private static Map<String, Method> scanGetters(Class<?> clazz) {
        Map<String, Method> result = new LinkedHashMap<>();
        for (Method m : clazz.getMethods()) {
            if (m.getParameterCount() != 0 || m.getReturnType() == void.class) continue;
            String name = m.getName();
            if (SKIP_METHODS.contains(name)) continue;

            String field = fieldName(name);
            if (field != null && !field.isEmpty()) {
                result.put(field, m);
            }
        }
        return result;
    }

    private static Map<String, Method> scanSetters(Class<?> clazz) {
        Map<String, Method> result = new HashMap<>();
        for (Method m : clazz.getMethods()) {
            String name = m.getName();
            if (m.getParameterCount() != 1 || !name.startsWith("set") || name.length() <= 3) continue;
            result.put(decapitalize(name.substring(3)), m);
        }
        return result;
    }

    private static String fieldName(String methodName) {
        if (methodName.startsWith("get") && methodName.length() > 3) {
            return decapitalize(methodName.substring(3));
        }
        if (methodName.startsWith("is") && methodName.length() > 2
                && Character.isUpperCase(methodName.charAt(2))) {
            return decapitalize(methodName.substring(2));
        }
        return null;
    }

    private static String decapitalize(String s) {
        if (s.isEmpty()) return s;
        char[] chars = s.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }
}
