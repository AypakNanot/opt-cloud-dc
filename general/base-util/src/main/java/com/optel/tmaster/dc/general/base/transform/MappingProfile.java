/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.base.transform;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Declarative mapping configuration loaded from JSON.
 *
 * <h3>JSON format</h3>
 * <pre>{@code
 * {
 *   "name": "osu-connection",
 *   "sourceType": "com.optel.devconf.opt.connection.CreateOsuConnectionNeInput",
 *   "targetBuilder": "ccsa.yang.acc.osu.CreateOsuConnectionInputBuilder",
 *   "autoFields": ["connectionName", "frequency", "rate"],
 *   "renameFields": { "adminStatus": "adminState" },
 *   "skipFields": ["neId", "yangMode"],
 *   "nestedMappingEnabled": true,
 *   "withKeyDetection": true
 * }
 * }</pre>
 */
public final class MappingProfile {

    private String name;
    private String sourceType;
    private String targetBuilder;
    private List<String> autoFields = Collections.emptyList();
    private Map<String, String> renameFields = Collections.emptyMap();
    private List<String> skipFields = Collections.emptyList();
    private boolean nestedMappingEnabled = true;
    private boolean withKeyDetection = true;

    // Required for JSON deserialization
    public MappingProfile() { }

    // ─── Getters / Setters (for hutool JSON binding) ─────

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSourceType() { return sourceType; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }

    public String getTargetBuilder() { return targetBuilder; }
    public void setTargetBuilder(String targetBuilder) { this.targetBuilder = targetBuilder; }

    public List<String> getAutoFields() { return autoFields; }
    public void setAutoFields(List<String> autoFields) { this.autoFields = autoFields; }

    public Map<String, String> getRenameFields() { return renameFields; }
    public void setRenameFields(Map<String, String> renameFields) { this.renameFields = renameFields; }

    public List<String> getSkipFields() { return skipFields; }
    public void setSkipFields(List<String> skipFields) { this.skipFields = skipFields; }

    public boolean isNestedMappingEnabled() { return nestedMappingEnabled; }
    public void setNestedMappingEnabled(boolean nestedMappingEnabled) { this.nestedMappingEnabled = nestedMappingEnabled; }

    public boolean isWithKeyDetection() { return withKeyDetection; }
    public void setWithKeyDetection(boolean withKeyDetection) { this.withKeyDetection = withKeyDetection; }

    // ─── Builder for programmatic creation ───────────────

    public static Builder builder(String name) {
        return new Builder(name);
    }

    public static class Builder {
        private final MappingProfile profile = new MappingProfile();

        Builder(String name) { profile.name = name; }

        public Builder sourceType(String s) { profile.sourceType = s; return this; }
        public Builder targetBuilder(String s) { profile.targetBuilder = s; return this; }
        public Builder autoFields(List<String> f) { profile.autoFields = f; return this; }
        public Builder renameFields(Map<String, String> r) { profile.renameFields = r; return this; }
        public Builder skipFields(List<String> s) { profile.skipFields = s; return this; }
        public Builder nestedMappingEnabled(boolean b) { profile.nestedMappingEnabled = b; return this; }
        public Builder withKeyDetection(boolean b) { profile.withKeyDetection = b; return this; }

        public MappingProfile build() { return profile; }
    }

    @Override
    public String toString() {
        return "MappingProfile{name=" + name + ", source=" + sourceType + ", target=" + targetBuilder
            + ", autoFields=" + autoFields.size() + ", renames=" + renameFields.size()
            + ", skips=" + skipFields.size() + "}";
    }
}
