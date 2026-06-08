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
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.optel.tmaster.dc.general.base.transform.fixture.TestFixtures.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for config-driven mapping: {@link MappingProfileLoader} + {@link ConfigDrivenMapper}.
 */
@DisplayName("Config-Driven Mapping")
class ConfigDrivenMapperTest {

    @AfterEach
    void tearDown() {
        MappingProfileLoader.clearCache();
        ConfigDrivenMapper.reset();
        MappingEngine.reset();
    }

    // ─── MappingProfileLoader ────────────────────────────────

    @Nested
    @DisplayName("MappingProfileLoader")
    class ProfileLoaderTests {

        @Test
        @DisplayName("loads profile from classpath JSON")
        void loadsFromClasspath() {
            MappingProfile profile = MappingProfileLoader.loadFromClasspath(
                "transform/device-mapping.json");

            assertNotNull(profile);
            assertEquals("device-mapping", profile.getName());
            assertEquals(6, profile.getAutoFields().size());
            assertTrue(profile.getAutoFields().contains("name"));
            assertTrue(profile.getAutoFields().contains("status"));
            assertEquals(1, profile.getSkipFields().size());
            assertEquals("priority", profile.getSkipFields().get(0));
        }

        @Test
        @DisplayName("loads profile with rename fields")
        void loadsRenameProfile() {
            MappingProfile profile = MappingProfileLoader.loadFromClasspath(
                "transform/rename-mapping.json");

            assertEquals("rename-mapping", profile.getName());
            assertEquals(2, profile.getRenameFields().size());
            assertEquals("adminState", profile.getRenameFields().get("adminStatus"));
            assertEquals("operState", profile.getRenameFields().get("operStatus"));
        }

        @Test
        @DisplayName("profile is cached (same instance on second load)")
        void profileIsCached() {
            MappingProfile p1 = MappingProfileLoader.loadFromClasspath(
                "transform/device-mapping.json");
            MappingProfile p2 = MappingProfileLoader.loadFromClasspath(
                "transform/device-mapping.json");
            // Cached — should be the same object
            assertEquals(p1, p2);
        }
    }

    // ─── ConfigDrivenMapper ──────────────────────────────────

    @Nested
    @DisplayName("ConfigDrivenMapper")
    class ConfigDrivenMapperTests {

        @Test
        @DisplayName("maps source to built target using profile")
        void mapsUsingProfile() {
            DeviceSource source = new DeviceSource() {
                public String getName() { return "dev-cfg"; }
                public String getStatus() { return "up"; }
                public Integer getPriority() { return 999; }
                public String getDescription() { return "from config"; }
                public String getLocation() { return "lab"; }
                public Boolean getEnabled() { return false; }
                public Integer getPortCount() { return 24; }
            };

            MappingProfile profile = MappingProfileLoader.loadFromClasspath(
                "transform/device-mapping.json");

            Object result = ConfigDrivenMapper.map(source, profile);
            assertTrue(result instanceof DeviceTarget);
            DeviceTarget target = (DeviceTarget) result;

            assertEquals("dev-cfg", target.name());
            assertEquals("up", target.status());
            assertEquals("from config", target.description());
            assertEquals("lab", target.location());
            assertEquals(false, target.enabled());
            assertEquals(24, target.portCount());
            // priority is in skipFields → not copied
            assertNull(target.adminState());
        }

        @Test
        @DisplayName("mapToBuilder returns unbuilt builder for further adjustment")
        void mapToBuilderAllowsManualAdjustment() {
            DeviceSource source = new DeviceSource() {
                public String getName() { return "dev"; }
                public String getStatus() { return "down"; }
                public Integer getPriority() { return 0; }
                public String getDescription() { return null; }
                public String getLocation() { return null; }
                public Boolean getEnabled() { return null; }
                public Integer getPortCount() { return null; }
            };

            MappingProfile profile = MappingProfileLoader.loadFromClasspath(
                "transform/device-mapping.json");

            Object builder = ConfigDrivenMapper.mapToBuilder(source, profile);
            assertTrue(builder instanceof DeviceTargetBuilder);

            // Manual adjustment
            DeviceTargetBuilder tb = (DeviceTargetBuilder) builder;
            tb.setAdminState("manual-override");

            DeviceTarget target = tb.build();
            assertEquals("dev", target.name());
            assertEquals("down", target.status());
            assertEquals("manual-override", target.adminState());
        }

        @Test
        @DisplayName("rename fields are properly mapped")
        void renameFieldsWork() {
            RenameSource source = new RenameSource() {
                public String getAdminStatus() { return "locked"; }
                public String getOperStatus() { return "active"; }
            };

            MappingProfile profile = MappingProfileLoader.loadFromClasspath(
                "transform/rename-mapping.json");

            Object result = ConfigDrivenMapper.map(source, profile);
            DeviceTarget target = (DeviceTarget) result;

            assertEquals("locked", target.adminState());
            assertEquals("active", target.operState());
        }

        @Test
        @DisplayName("null source returns null")
        void nullSourceReturnsNull() {
            MappingProfile profile = MappingProfileLoader.loadFromClasspath(
                "transform/device-mapping.json");
            assertNull(ConfigDrivenMapper.map(null, profile));
        }

        @Test
        @DisplayName("null profile returns null")
        void nullProfileReturnsNull() {
            DeviceSource source = new DeviceSource() {
                public String getName() { return "x"; }
                public String getStatus() { return "x"; }
                public Integer getPriority() { return 0; }
                public String getDescription() { return null; }
                public String getLocation() { return null; }
                public Boolean getEnabled() { return null; }
                public Integer getPortCount() { return null; }
            };
            assertNull(ConfigDrivenMapper.map(source, null));
        }

        @Test
        @DisplayName("named converter can be registered and retrieved")
        void namedConverterRegistration() {
            ConfigDrivenMapper.registerConverter("test-conv",
                (ObjectConverter<String, Integer>) Integer::parseInt);
            assertNotNull(ConfigDrivenMapper.getNamedConverter("test-conv"));
        }
    }

    // ─── MappingProfile builder ──────────────────────────────

    @Nested
    @DisplayName("MappingProfile Builder")
    class ProfileBuilderTests {

        @Test
        @DisplayName("builder creates valid profile programmatically")
        void builderCreatesProfile() {
            MappingProfile profile = MappingProfile.builder("test")
                .sourceType("com.example.Source")
                .targetBuilder("com.example.TargetBuilder")
                .autoFields(java.util.List.of("a", "b"))
                .renameFields(java.util.Map.of("oldName", "newName"))
                .skipFields(java.util.List.of("x"))
                .build();

            assertEquals("test", profile.getName());
            assertEquals("com.example.Source", profile.getSourceType());
            assertEquals("com.example.TargetBuilder", profile.getTargetBuilder());
            assertEquals(2, profile.getAutoFields().size());
            assertEquals(1, profile.getRenameFields().size());
            assertEquals(1, profile.getSkipFields().size());
        }
    }
}
