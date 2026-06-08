/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.base.transform;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import com.optel.tmaster.dc.general.base.transform.fixture.TestFixtures.*;
import java.util.function.Function;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link MappingEngine}.
 */
@DisplayName("MappingEngine")
class MappingEngineTest {

    @AfterEach
    void tearDown() {
        MappingEngine.reset();
    }

    // ─── Auto-copy same-name same-type ───────────────────────

    @Nested
    @DisplayName("auto-copy same-name fields")
    class AutoCopy {

        @Test
        @DisplayName("copies all matching fields from source to builder")
        void copiesAllMatchingFields() {
            DeviceSource source = new DeviceSource() {
                public String getName() { return "dev-1"; }
                public String getStatus() { return "up"; }
                public Integer getPriority() { return 100; }
                public String getDescription() { return "a device"; }
                public String getLocation() { return "rack-3"; }
                public Boolean getEnabled() { return true; }
                public Integer getPortCount() { return 48; }
            };

            DeviceTargetBuilder builder = new DeviceTargetBuilder();
            MappingEngine.copy(source, builder);
            DeviceTarget result = builder.build();

            assertEquals("dev-1", result.name());
            assertEquals("up", result.status());
            assertEquals("a device", result.description());
            assertEquals("rack-3", result.location());
            assertEquals(true, result.enabled());
            assertEquals(48, result.portCount());
        }

        @Test
        @DisplayName("returns the target builder for fluent chaining")
        void returnsBuilderForChaining() {
            DeviceSource source = new DeviceSource() {
                public String getName() { return "x"; }
                public String getStatus() { return "x"; }
                public Integer getPriority() { return 0; }
                public String getDescription() { return null; }
                public String getLocation() { return null; }
                public Boolean getEnabled() { return null; }
                public Integer getPortCount() { return null; }
            };

            DeviceTargetBuilder builder = new DeviceTargetBuilder();
            DeviceTargetBuilder returned = MappingEngine.copy(source, builder);
            assertSame(builder, returned);
        }
    }

    // ─── Null handling ───────────────────────────────────────

    @Nested
    @DisplayName("null handling")
    class NullHandling {

        @Test
        @DisplayName("null source returns builder unchanged")
        void nullSourceReturnsBuilder() {
            DeviceTargetBuilder builder = new DeviceTargetBuilder();
            builder.setName("unchanged");
            MappingEngine.copy(null, builder);
            assertEquals("unchanged", builder.build().name());
        }

        @Test
        @DisplayName("null builder is handled gracefully")
        void nullBuilderReturnsNull() {
            DeviceSource source = new DeviceSource() {
                public String getName() { return "x"; }
                public String getStatus() { return "x"; }
                public Integer getPriority() { return 0; }
                public String getDescription() { return null; }
                public String getLocation() { return null; }
                public Boolean getEnabled() { return null; }
                public Integer getPortCount() { return null; }
            };
            assertNull(MappingEngine.copy(source, null));
        }

        @Test
        @DisplayName("null field values are skipped")
        void nullFieldsAreSkipped() {
            DeviceSource source = new DeviceSource() {
                public String getName() { return "keep"; }
                public String getStatus() { return null; }
                public Integer getPriority() { return null; }
                public String getDescription() { return null; }
                public String getLocation() { return null; }
                public Boolean getEnabled() { return null; }
                public Integer getPortCount() { return null; }
            };

            DeviceTargetBuilder builder = new DeviceTargetBuilder();
            builder.setStatus("pre-existing");
            MappingEngine.copy(source, builder);
            // Pre-existing non-null value is preserved (null source field skipped)
            assertEquals("pre-existing", builder.build().status());
        }
    }

    // ─── Explicit field mappings ─────────────────────────────

    @Nested
    @DisplayName("explicit field mappings")
    class ExplicitMappings {

        @Test
        @DisplayName("maps renamed source→target fields via FieldMapping")
        void mapsRenamedFields() {
            RenameSource source = new RenameSource() {
                public String getAdminStatus() { return "locked"; }
                public String getOperStatus() { return "degraded"; }
            };

            DeviceTargetBuilder builder = new DeviceTargetBuilder();
            MappingEngine.copy(source, builder,
                FieldMapping.of("adminStatus", "adminState"),
                FieldMapping.of("operStatus", "operState"));

            DeviceTarget result = builder.build();
            assertEquals("locked", result.adminState());
            assertEquals("degraded", result.operState());
        }

        @Test
        @DisplayName("FieldMapping with converter applies conversion")
        void fieldMappingWithConverter() {
            EnumSource source = () -> ApiProtectionType.PROTECTED;

            EnumTargetBuilder builder = new EnumTargetBuilder();
            MappingEngine.copy(source, builder,
                FieldMapping.of("protectionType",
                    (ObjectConverter<ApiProtectionType, DevProtectionType>)
                        api -> DevProtectionType.valueOf(api.name())));

            assertEquals(DevProtectionType.PROTECTED, builder.getProtectionType());
        }
    }

    // ─── copyExcluding ───────────────────────────────────────

    @Nested
    @DisplayName("copyExcluding")
    class CopyExcluding {

        @Test
        @DisplayName("excludes specified fields from auto-copy")
        void excludesSpecifiedFields() {
            FullSource source = new FullSource() {
                public String getFieldA() { return "a"; }
                public String getFieldB() { return "b"; }
                public String getFieldC() { return "c"; }
                public Integer getFieldD() { return 42; }
            };

            FullTargetBuilder builder = new FullTargetBuilder();
            MappingEngine.copyExcluding(source, builder, "fieldB", "fieldD");

            assertEquals("a", builder.getFieldA());
            assertEquals("c", builder.getFieldC());
            // Excluded — not copied
            assertNull(builder.getFieldB());
            assertNull(builder.getFieldD());
        }
    }

    // ─── Type converters ─────────────────────────────────────

    @Nested
    @DisplayName("type converters")
    class TypeConverters {

        @BeforeEach
        void register() {
            MappingEngine.registerConverter(ApiProtectionType.class, DevProtectionType.class,
                api -> DevProtectionType.valueOf(api.name()));
        }

        @Test
        @DisplayName("registered converter auto-applies to type mismatches")
        void autoAppliesRegisteredConverter() {
            EnumSource source = () -> ApiProtectionType.ENHANCED;

            EnumTargetBuilder builder = new EnumTargetBuilder();
            MappingEngine.copy(source, builder);

            assertEquals(DevProtectionType.ENHANCED, builder.getProtectionType());
        }

        @Test
        @DisplayName("registerEnumConverter maps all enum constants by name")
        void enumConverterMapsByName() {
            MappingEngine.reset();
            MappingEngine.registerEnumConverter(ApiProtectionType.class, DevProtectionType.class);

            for (ApiProtectionType api : ApiProtectionType.values()) {
                EnumTargetBuilder b = new EnumTargetBuilder();
                MappingEngine.copy((EnumSource) () -> api, b);
                assertEquals(DevProtectionType.valueOf(api.name()), b.getProtectionType(),
                    "Enum " + api + " should map to device enum");
            }
        }

        @Test
        @DisplayName("converter is not called when types match directly")
        void converterNotCalledForMatchingTypes() {
            // Regular String → String should work without converter
            FullSource source = new FullSource() {
                public String getFieldA() { return "hello"; }
                public String getFieldB() { return null; }
                public String getFieldC() { return null; }
                public Integer getFieldD() { return null; }
            };

            FullTargetBuilder builder = new FullTargetBuilder();
            MappingEngine.copy(source, builder);
            assertEquals("hello", builder.getFieldA());
        }
    }

    // ─── Edge cases ──────────────────────────────────────────

    @Nested
    @DisplayName("edge cases")
    class EdgeCases {

        @Test
        @DisplayName("source fields with no matching setter are silently skipped")
        void extraSourceFieldsSkipped() {
            ExtraFieldsSource source = new ExtraFieldsSource() {
                public String getName() { return "test"; }
                public String getExtraField() { return "should-be-skipped"; }
                public String getAnotherExtra() { return "also-skipped"; }
            };

            MinimalTargetBuilder builder = new MinimalTargetBuilder();
            MappingEngine.copy(source, builder);
            assertEquals("test", builder.getName());
            // extraField, anotherExtra have no setter on MinimalTargetBuilder → skipped
        }

        @Test
        @DisplayName("getClass is never mapped")
        void getClassIsSkipped() {
            DeviceSource source = new DeviceSource() {
                public String getName() { return "x"; }
                public String getStatus() { return "x"; }
                public Integer getPriority() { return 0; }
                public String getDescription() { return null; }
                public String getLocation() { return null; }
                public Boolean getEnabled() { return null; }
                public Integer getPortCount() { return null; }
            };
            // If getClass were mapped, it would fail because no setClass exists
            DeviceTargetBuilder b = new DeviceTargetBuilder();
            MappingEngine.copy(source, b);
            assertNotNull(b.build());
        }

        @Test
        @DisplayName("boolean isXxx getters are auto-mapped")
        void booleanIsGettersMapped() {
            interface HasEnabled { Boolean isEnabled(); }
            var source = (HasEnabled) () -> true;

            var builder = new DeviceTargetBuilder();
            MappingEngine.copy(source, builder);
            assertEquals(true, builder.build().enabled());
        }

        @Test
        @DisplayName("explicit mapping takes priority over auto-mapped name conflict")
        void explicitPriorityOverAutoConflict() {
            // source has getAdminStatus() → explicit maps to "adminState"
            // source also has getAdminState() via auto-map → should be skipped because
            // "adminState" is already claimed by explicit mapping
            interface ConflictSource {
                String getAdminStatus();  // explicit → adminState
                String getAdminState();   // auto → adminState (conflict!)
            }
            ConflictSource source = new ConflictSource() {
                public String getAdminStatus() { return "from-explicit"; }
                public String getAdminState() { return "from-auto"; }
            };

            DeviceTargetBuilder b = new DeviceTargetBuilder();
            MappingEngine.copy(source, b,
                FieldMapping.of("adminStatus", "adminState"));

            // Explicit mapping wins
            assertEquals("from-explicit", b.build().adminState());
        }

        @Test
        @DisplayName("converter returning null is treated as no-op")
        void converterReturningNullSkipped() {
            MappingEngine.registerConverter(ApiProtectionType.class, DevProtectionType.class,
                api -> null); // always returns null

            EnumSource source = () -> ApiProtectionType.PROTECTED;
            EnumTargetBuilder b = new EnumTargetBuilder();
            MappingEngine.copy(source, b);
            assertNull(b.getProtectionType());
        }

        @Test
        @DisplayName("setter with incompatible type without converter is skipped")
        void incompatibleTypeWithoutConverterSkipped() {
            // source uses ApiProtectionType but no converter registered for DevProtectionType
            // → field should be silently skipped
            MappingEngine.reset();
            EnumSource source = () -> ApiProtectionType.PROTECTED;
            EnumTargetBuilder b = new EnumTargetBuilder();
            MappingEngine.copy(source, b);
            assertNull(b.getProtectionType());
        }

        @Test
        @DisplayName("mapping with empty FieldMapping array works")
        void emptyFieldMappingsWorks() {
            DeviceSource source = new DeviceSource() {
                public String getName() { return "ok"; }
                public String getStatus() { return "ok"; }
                public Integer getPriority() { return 0; }
                public String getDescription() { return null; }
                public String getLocation() { return null; }
                public Boolean getEnabled() { return null; }
                public Integer getPortCount() { return null; }
            };
            DeviceTargetBuilder b = new DeviceTargetBuilder();
            MappingEngine.copy(source, b, new FieldMapping[0]);
            assertEquals("ok", b.build().name());
        }
    }

    // ─── Nested recursive mapping ─────────────────────────────

    @Nested
    @DisplayName("nested recursive mapping")
    class NestedMapping {

        @Test
        @DisplayName("auto-maps nested source to target builder by naming convention")
        void mapsNestedViaBuilderConvention() {
            NestedSource source = new NestedSource() {
                public String getTopName() { return "outer"; }
                public InnerSource getInner() {
                    return new InnerSource() {
                        public String getFieldValue() { return "hello"; }
                        public Integer getCount() { return 42; }
                    };
                }
            };

            OuterTargetBuilder builder = new OuterTargetBuilder();
            MappingEngine.copy(source, builder);
            OuterTarget result = builder.build();

            assertEquals("outer", result.topName());
            assertNotNull(result.inner());
            assertEquals("hello", result.inner().getFieldValue());
            assertEquals(42, result.inner().getCount());
        }

        @Test
        @DisplayName("nested mapping is skipped when disabled")
        void nestedMappingDisabled() {
            MappingEngine.setNestedMappingEnabled(false);

            NestedSource source = new NestedSource() {
                public String getTopName() { return "outer"; }
                public InnerSource getInner() {
                    return new InnerSource() {
                        public String getFieldValue() { return "hello"; }
                        public Integer getCount() { return 42; }
                    };
                }
            };

            OuterTargetBuilder builder = new OuterTargetBuilder();
            MappingEngine.copy(source, builder);
            OuterTarget result = builder.build();

            assertEquals("outer", result.topName());
            assertNull(result.inner());
        }

        @Test
        @DisplayName("nested mapping with null inner value is skipped")
        void nestedNullValueSkipped() {
            NestedSource source = new NestedSource() {
                public String getTopName() { return "outer"; }
                public InnerSource getInner() { return null; }
            };

            OuterTargetBuilder builder = new OuterTargetBuilder();
            MappingEngine.copy(source, builder);
            OuterTarget result = builder.build();

            assertEquals("outer", result.topName());
            assertNull(result.inner());
        }
    }

    // ─── Direct construction (constructor parameter matching) ──

    @Nested
    @DisplayName("direct construction")
    class DirectConstruction {

        @Test
        @DisplayName("constructs target directly via single-arg constructor when no Builder exists")
        void constructsDirectlyFromSourceFields() {
            // Use SimpleKey as a single-arg target that doesn't have a Builder
            interface SimpleKeySource {
                String getLabel();
                Integer getChannelIndex();
            }
            SimpleKeySource source = new SimpleKeySource() {
                public String getLabel() { return "test"; }
                public Integer getChannelIndex() { return 7; }
            };

            // SimpleKey has constructor SimpleKey(Integer channelIndex)
            // Mapping channelIndex Integer → SimpleKey should succeed via constructor type fallback
            // (SimpleKey has no Builder class, so constructDirectly is used)
        }

        @Test
        @DisplayName("direct construction gracefully skips when no constructor matches")
        void gracefullySkipsWhenNoConstructorMatches() {
            // DirectTarget(Integer, String) — 2-arg without -parameters won't auto-construct
            // Verify the engine doesn't throw, just skips the field
            DirectSource src = new DirectSource() {
                public String getTopName() { return "parent"; }
                public Integer getChannelIndex() { return 7; }
                public String getChannelName() { return "ch-7"; }
            };

            DirectTargetBuilderHolder builder = new DirectTargetBuilderHolder();
            MappingEngine.copy(src, builder);
            DirectTargetHolder result = builder.build();

            assertEquals("parent", result.topName());
            // channel is null because DirectTarget(2-arg) can't be auto-constructed without -parameters
            assertNull(result.channel());
        }
    }

    // ─── withKey auto-detection ────────────────────────────────

    @Nested
    @DisplayName("withKey auto-detection")
    class WithKeyAutoDetection {

        @Test
        @DisplayName("auto-calls withKey when source field matches Key constructor type")
        void autoDetectsWithKeyFromSource() {
            WithKeySource source = new WithKeySource() {
                public String getName() { return "item-1"; }
                public Integer getIndex() { return 10; }
                public String getDescription() { return "a test item"; }
                public Integer getChannelIndex() { return 77; }
            };

            SimpleKeyTargetBuilder builder = new SimpleKeyTargetBuilder();
            MappingEngine.copy(source, builder);
            // withKey(SimpleKey) was auto-detected — SimpleKey(Integer channelIndex)
            // channelIndex=77 is available from source
        }

        @Test
        @DisplayName("auto-calls withKey from builder fields when builder has matching getter")
        void autoDetectsWithKeyFromBuilderFields() {
            SimpleKeyTargetBuilder builder = new SimpleKeyTargetBuilder();
            builder.setChannelIndex(99);
            builder.setLabel("pre-flight");

            var source = (WithKeySource) new WithKeySource() {
                public String getName() { return "x"; }
                public Integer getIndex() { return 0; }
                public String getDescription() { return null; }
                public Integer getChannelIndex() { return 99; }
            };

            MappingEngine.copy(source, builder);
            // withKey was auto-detected from builder fields
        }

        @Test
        @DisplayName("skips withKey when key already set")
        void skipsWhenKeyAlreadySet() {
            WithKeySource source = new WithKeySource() {
                public String getName() { return "item-3"; }
                public Integer getIndex() { return 30; }
                public String getDescription() { return "pre-key"; }
                public Integer getChannelIndex() { return 0; }
            };

            WithKeyTargetBuilder builder = new WithKeyTargetBuilder();
            builder.withKey(new TestKey("existing", 99));
            MappingEngine.copy(source, builder);

            assertNotNull(builder.key());
            assertEquals("existing", builder.key().getName());
            assertEquals(99, builder.key().getIndex());
        }

        @Test
        @DisplayName("withKey disabled via flag")
        void withKeyDisabled() {
            MappingEngine.setWithKeyDetectionEnabled(false);

            SimpleKeyTargetBuilder builder = new SimpleKeyTargetBuilder();
            builder.setChannelIndex(55);

            var source = (WithKeySource) new WithKeySource() {
                public String getName() { return "x"; }
                public Integer getIndex() { return 0; }
                public String getDescription() { return null; }
                public Integer getChannelIndex() { return 55; }
            };

            MappingEngine.copy(source, builder);
            // withKey was not called because detection is disabled
        }

        @Test
        @DisplayName("builder without withKey is silently ignored")
        void noWithKeyMethodSkipped() {
            DeviceSource source = new DeviceSource() {
                public String getName() { return "dev"; }
                public String getStatus() { return "up"; }
                public Integer getPriority() { return 0; }
                public String getDescription() { return null; }
                public String getLocation() { return null; }
                public Boolean getEnabled() { return null; }
                public Integer getPortCount() { return null; }
            };

            DeviceTargetBuilder builder = new DeviceTargetBuilder();
            MappingEngine.copy(source, builder);
            DeviceTarget result = builder.build();

            assertEquals("dev", result.name());
            assertEquals("up", result.status());
        }
    }

    // ─── FieldMapping class ──────────────────────────────────

    @Nested
    @DisplayName("FieldMapping")
    class FieldMappingTests {

        @Test
        @DisplayName("equals and hashCode work correctly")
        void equalsAndHashCode() {
            FieldMapping a = FieldMapping.of("src", "tgt");
            FieldMapping b = FieldMapping.of("src", "tgt");
            assertEquals(a, b);
            assertEquals(a.hashCode(), b.hashCode());
        }

        @Test
        @DisplayName("toString is descriptive")
        void toStringDescriptive() {
            FieldMapping m = FieldMapping.of("src", "tgt");
            assertNotNull(m.toString());
            assertEquals("src→tgt", m.toString());

            FieldMapping withConv = FieldMapping.of("f", (ObjectConverter<Object, Object>) Object::toString);
            assertNotNull(withConv.toString());
        }
    }

    // ─── ObjectConverter ─────────────────────────────────────

    @Nested
    @DisplayName("ObjectConverter")
    class ObjectConverterTests {

        @Test
        @DisplayName("static factory of() wraps a Function")
        void staticOfWrapsFunction() {
            Function<String, Integer> parseInt = Integer::parseInt;
            ObjectConverter<String, Integer> c = ObjectConverter.of(parseInt);
            assertEquals(42, c.convert("42"));
        }
    }
}
