/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.base.transform;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Applies a {@link MappingProfile} (loaded from JSON config) to map source → target.
 *
 * <h3>Usage</h3>
 * <pre>{@code
 * // 1. Register converters at startup
 * ConfigDrivenMapper.registerConverter("protType", api -> DevProtectionType.valueOf(api.name()));
 *
 * // 2. Load mapping profiles from JSON config files
 * MappingProfile profile = MappingProfileLoader.loadFromClasspath("transform/osu-connection.json");
 *
 * // 3. Use in transform code
 * DevInput result = ConfigDrivenMapper.map(input, profile);
 * }</pre>
 */
public final class ConfigDrivenMapper {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigDrivenMapper.class);

    // Named converters accessible from JSON config (not used in JSON yet, but available programmatically)
    private static final Map<String, ObjectConverter<Object, Object>> NAMED_CONVERTERS =
        new ConcurrentHashMap<>();

    // Builder build() method cache
    private static final Map<Class<?>, Method> BUILD_METHOD_CACHE = new ConcurrentHashMap<>();

    private ConfigDrivenMapper() { }

    /**
     * Register a named converter for use in mapping profiles.
     */
    @SuppressWarnings("unchecked")
    public static <S, T> void registerConverter(String name, ObjectConverter<S, T> converter) {
        NAMED_CONVERTERS.put(name, (ObjectConverter<Object, Object>) converter);
        LOG.debug("Registered named converter: {}", name);
    }

    /**
     * Map source to a fully-built target object using the given profile.
     *
     * @param source  the source data object
     * @param profile the mapping profile (from JSON config)
     * @return the built target object
     */
    public static Object map(Object source, MappingProfile profile) {
        Object builder = mapToBuilder(source, profile);
        if (builder == null) {
            return null;
        }
        return build(builder);
    }

    /**
     * Map source to a target builder (unbuilt), allowing further manual adjustments.
     *
     * @param source  the source data object
     * @param profile the mapping profile (from JSON config)
     * @return the target builder instance
     */
    public static Object mapToBuilder(Object source, MappingProfile profile) {
        if (source == null || profile == null) {
            return null;
        }

        Class<?> builderClass;
        try {
            builderClass = Class.forName(profile.getTargetBuilder());
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(
                "Target builder class not found: " + profile.getTargetBuilder(), e);
        }

        Object builder;
        try {
            builder = builderClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalArgumentException(
                "Failed to instantiate builder: " + profile.getTargetBuilder(), e);
        }

        // Build the set of fields to exclude
        Set<String> excludedFields = new HashSet<>(profile.getSkipFields());

        // Build explicit FieldMappings for renamed fields
        List<FieldMapping> fieldMappings = new ArrayList<>();
        for (var entry : profile.getRenameFields().entrySet()) {
            String sourceField = entry.getKey();
            String targetField = entry.getValue();
            fieldMappings.add(FieldMapping.of(sourceField, targetField));
            excludedFields.add(targetField);
        }

        // Apply profile-level feature flags
        boolean prevNested = MappingEngine.isNestedMappingEnabled();
        boolean prevWithKey = MappingEngine.isWithKeyDetectionEnabled();
        MappingEngine.setNestedMappingEnabled(profile.isNestedMappingEnabled());
        MappingEngine.setWithKeyDetectionEnabled(profile.isWithKeyDetection());

        try {
            // If autoFields is specified, only copy those fields
            if (!profile.getAutoFields().isEmpty()) {
                for (String field : profile.getAutoFields()) {
                    if (excludedFields.contains(field)) {
                        continue;
                    }
                    fieldMappings.add(FieldMapping.of(field, field));
                }
                MappingEngine.copy(source, builder, fieldMappings.toArray(new FieldMapping[0]));
            } else {
                MappingEngine.copy(source, builder, fieldMappings.toArray(new FieldMapping[0]));
            }
        } finally {
            MappingEngine.setNestedMappingEnabled(prevNested);
            MappingEngine.setWithKeyDetectionEnabled(prevWithKey);
        }

        return builder;
    }

    /**
     * Get a registered named converter.
     */
    public static ObjectConverter<Object, Object> getNamedConverter(String name) {
        return NAMED_CONVERTERS.get(name);
    }

    /**
     * Clear all named converters and caches (for testing).
     */
    public static void reset() {
        NAMED_CONVERTERS.clear();
        BUILD_METHOD_CACHE.clear();
    }

    private static Object build(Object builder) {
        Method buildMethod = BUILD_METHOD_CACHE.computeIfAbsent(builder.getClass(), cls -> {
            try {
                return cls.getMethod("build");
            } catch (NoSuchMethodException e) {
                throw new IllegalArgumentException(
                    "Builder " + cls.getName() + " has no build() method", e);
            }
        });
        try {
            return buildMethod.invoke(builder);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to call build() on " + builder.getClass().getName(), e);
        }
    }
}
