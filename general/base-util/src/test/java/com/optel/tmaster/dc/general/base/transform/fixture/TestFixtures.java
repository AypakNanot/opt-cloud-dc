/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.base.transform.fixture;

/**
 * Test fixtures mimicking YANG-generated beans for MappingEngine tests.
 */

// ─── Source beans (API side, immutable interfaces with getters) ───

interface HasName { String getName(); }
interface HasStatus { String getStatus(); }
interface HasPriority { Integer getPriority(); }

public final class TestFixtures {

    private TestFixtures() { }

    // ── Simple source bean ──────────────────────────────────

    public interface DeviceSource extends HasName, HasStatus, HasPriority {
        String getDescription();
        String getLocation();
        Boolean getEnabled();
        Integer getPortCount();
    }

    // ── Simple target builder ───────────────────────────────

    public static class DeviceTargetBuilder {
        private String name, status, description, location;
        private Boolean enabled;
        private Integer portCount;
        private String adminState;
        private String operState;

        public DeviceTargetBuilder setName(String v) { this.name = v; return this; }
        public DeviceTargetBuilder setStatus(String v) { this.status = v; return this; }
        public DeviceTargetBuilder setDescription(String v) { this.description = v; return this; }
        public DeviceTargetBuilder setLocation(String v) { this.location = v; return this; }
        public DeviceTargetBuilder setEnabled(Boolean v) { this.enabled = v; return this; }
        public DeviceTargetBuilder setPortCount(Integer v) { this.portCount = v; return this; }
        public DeviceTargetBuilder setAdminState(String v) { this.adminState = v; return this; }
        public DeviceTargetBuilder setOperState(String v) { this.operState = v; return this; }

        public DeviceTarget build() {
            return new DeviceTarget(name, status, description, location, enabled, portCount, adminState, operState);
        }
    }

    public record DeviceTarget(String name, String status, String description, String location,
                                Boolean enabled, Integer portCount, String adminState, String operState) { }

    // ── Source with renamed fields ───────────────────────────

    public interface RenameSource {
        String getAdminStatus();   // → adminState on target
        String getOperStatus();    // → operState on target
    }

    // ── Enum types for converter tests ───────────────────────

    public enum ApiProtectionType { UNPROTECTED, PROTECTED, ENHANCED }
    public enum DevProtectionType { UNPROTECTED, PROTECTED, ENHANCED }

    public interface EnumSource {
        ApiProtectionType getProtectionType();
    }

    public static class EnumTargetBuilder {
        private DevProtectionType protectionType;
        public EnumTargetBuilder setProtectionType(DevProtectionType v) { this.protectionType = v; return this; }
        public DevProtectionType getProtectionType() { return protectionType; }
    }

    // ── Source with extra fields not on target ───────────────

    public interface ExtraFieldsSource extends HasName {
        String getExtraField();
        String getAnotherExtra();
    }

    public static class MinimalTargetBuilder {
        private String name;
        public MinimalTargetBuilder setName(String v) { this.name = v; return this; }
        public String getName() { return name; }
    }

    // ── Source for skip/exclude tests ────────────────────────

    public interface FullSource {
        String getFieldA();
        String getFieldB();
        String getFieldC();
        Integer getFieldD();
    }

    public static class FullTargetBuilder {
        private String fieldA, fieldB, fieldC;
        private Integer fieldD;
        public FullTargetBuilder setFieldA(String v) { this.fieldA = v; return this; }
        public FullTargetBuilder setFieldB(String v) { this.fieldB = v; return this; }
        public FullTargetBuilder setFieldC(String v) { this.fieldC = v; return this; }
        public FullTargetBuilder setFieldD(Integer v) { this.fieldD = v; return this; }
        public String getFieldA() { return fieldA; }
        public String getFieldB() { return fieldB; }
        public String getFieldC() { return fieldC; }
        public Integer getFieldD() { return fieldD; }
    }

    // ── withKey fixtures ───────────────────────────────────

    public static class TestKey {
        private final String name;
        private final Integer index;

        public TestKey(String name, Integer index) {
            this.name = name;
            this.index = index;
        }

        public String getName() { return name; }
        public Integer getIndex() { return index; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TestKey)) return false;
            TestKey k = (TestKey) o;
            return name.equals(k.name) && index.equals(k.index);
        }

        @Override
        public int hashCode() {
            return 31 * name.hashCode() + index.hashCode();
        }
    }

    /** Key with single-arg constructor matching only source fields. */
    public static class SimpleKey {
        private final Integer channelIndex;
        public SimpleKey(Integer channelIndex) { this.channelIndex = channelIndex; }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SimpleKey)) return false;
            return channelIndex.equals(((SimpleKey) o).channelIndex);
        }
        @Override
        public int hashCode() { return channelIndex.hashCode(); }
    }

    public interface WithKeySource {
        String getName();
        Integer getIndex();
        String getDescription();
        Integer getChannelIndex();
    }

    public static class WithKeyTargetBuilder {
        private String name;
        private Integer index;
        private String description;
        private TestKey key;

        public WithKeyTargetBuilder setName(String v) { this.name = v; return this; }
        public String getName() { return name; }
        public WithKeyTargetBuilder setIndex(Integer v) { this.index = v; return this; }
        public Integer getIndex() { return index; }
        public WithKeyTargetBuilder setDescription(String v) { this.description = v; return this; }

        public WithKeyTargetBuilder withKey(TestKey k) { this.key = k; return this; }
        public TestKey key() { return key; }

        public WithKeyTarget build() {
            return new WithKeyTarget(name, index, description, key);
        }
    }

    public record WithKeyTarget(String name, Integer index, String description, TestKey key) { }

    /** Builder that has withKey where key params come from builder fields. */
    public static class SimpleKeyTargetBuilder {
        private Integer channelIndex;
        private String label;

        public SimpleKeyTargetBuilder setChannelIndex(Integer v) { this.channelIndex = v; return this; }
        public Integer getChannelIndex() { return channelIndex; }
        public SimpleKeyTargetBuilder setLabel(String v) { this.label = v; return this; }

        public SimpleKeyTargetBuilder withKey(SimpleKey k) { return this; }
    }

    // ── Nested mapping fixtures ────────────────────────────

    public interface NestedSource {
        String getTopName();
        InnerSource getInner();
    }

    public interface InnerSource {
        String getFieldValue();
        Integer getCount();
    }

    /** Target type for nested object (the "built" result). */
    public static class InnerTarget {
        private final String fieldValue;
        private final Integer count;

        public InnerTarget(String fieldValue, Integer count) {
            this.fieldValue = fieldValue;
            this.count = count;
        }

        public String getFieldValue() { return fieldValue; }
        public Integer getCount() { return count; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof InnerTarget)) return false;
            InnerTarget t = (InnerTarget) o;
            return fieldValue.equals(t.fieldValue) && count.equals(t.count);
        }

        @Override
        public int hashCode() { return 31 * fieldValue.hashCode() + count.hashCode(); }
    }

    /** Builder for InnerTarget — found by naming convention: InnerTarget + "Builder". */
    public static class InnerTargetBuilder {
        private String fieldValue;
        private Integer count;

        public InnerTargetBuilder setFieldValue(String v) { this.fieldValue = v; return this; }
        public InnerTargetBuilder setCount(Integer v) { this.count = v; return this; }

        public InnerTarget build() { return new InnerTarget(fieldValue, count); }
    }

    public static class OuterTargetBuilder {
        private String topName;
        private InnerTarget inner;

        public OuterTargetBuilder setTopName(String v) { this.topName = v; return this; }
        public OuterTargetBuilder setInner(InnerTarget v) { this.inner = v; return this; }

        public OuterTarget build() { return new OuterTarget(topName, inner); }
    }

    public record OuterTarget(String topName, InnerTarget inner) { }

    // ── Direct construction fixtures (no Builder class) ────

    /** Target type without a corresponding Builder class. */
    public static class DirectTarget {
        private final Integer channelIndex;
        private final String channelName;

        public DirectTarget(Integer channelIndex, String channelName) {
            this.channelIndex = channelIndex;
            this.channelName = channelName;
        }

        public Integer getChannelIndex() { return channelIndex; }
        public String getChannelName() { return channelName; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof DirectTarget)) return false;
            DirectTarget t = (DirectTarget) o;
            return channelIndex.equals(t.channelIndex) && channelName.equals(t.channelName);
        }

        @Override
        public int hashCode() { return 31 * channelIndex.hashCode() + channelName.hashCode(); }
    }

    public interface DirectSource {
        String getTopName();
        Integer getChannelIndex();
        String getChannelName();
    }

    public static class DirectTargetBuilderHolder {
        private String topName;
        private DirectTarget channel;

        public DirectTargetBuilderHolder setTopName(String v) { this.topName = v; return this; }
        public DirectTargetBuilderHolder setChannel(DirectTarget v) { this.channel = v; return this; }

        public DirectTargetHolder build() { return new DirectTargetHolder(topName, channel); }
    }

    public record DirectTargetHolder(String topName, DirectTarget channel) { }
}
