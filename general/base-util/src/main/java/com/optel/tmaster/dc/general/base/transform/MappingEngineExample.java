/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.base.transform;

/**
 * Usage example: replacing manual transform code with MappingEngine.
 *
 * <h3>Scenario: OSU Connection transform (api → dev)</h3>
 *
 * <h3>Before (manual, ~80 lines):</h3>
 * <pre>{@code
 * public DevInput apiToDev(ApiInput input) {
 *     if (input == null) return null;
 *     DevBuilder b = new DevBuilder();
 *     b.setConnectionName(input.getConnectionName());
 *     b.setFrequency(input.getFrequency());
 *     b.setRate(input.getRate());
 *     b.setClientSignalType(apiClientSignalTypeToDev(input.getClientSignalType()));
 *     b.setProtectionType(apiProtectionTypeToDev(input.getProtectionType()));
 *     // ... 30 more same-name fields, 5 type conversions
 *     if (input.getPrimaryNni() != null) {
 *         NniBuilder nb = new NniBuilder();
 *         nb.setTpn(input.getPrimaryNni().getTpn());
 *         nb.setFtpName(input.getPrimaryNni().getFtpName());
 *         // ... more nested fields
 *         b.setPrimaryNni(nb.build());
 *     }
 *     return b.build();
 * }
 * }</pre>
 *
 * <h3>After (with MappingEngine):</h3>
 * <pre>{@code
 * // Startup: register enum converters once
 * static {
 *     MappingEngine.registerEnumConverter(ApiProtectionType.class, DevProtectionType.class);
 *     MappingEngine.registerEnumConverter(ApiSignalType.class, DevSignalType.class);
 *     // ... other enum pairs
 * }
 *
 * public DevInput apiToDev(ApiInput input) {
 *     if (input == null) return null;
 *     DevBuilder b = new DevBuilder();
 *
 *     // Auto-copy all same-name fields (handles 30+ fields in one call)
 *     MappingEngine.copy(input, b);
 *
 *     // Handle renamed fields manually
 *     b.setAdminState(input.getAdminStatus());
 *
 *     // Handle nested objects manually
 *     if (input.getPrimaryNni() != null) {
 *         NniBuilder nb = new NniBuilder();
 *         MappingEngine.copy(input.getPrimaryNni(), nb);
 *         b.setPrimaryNni(nb.build());
 *     }
 *
 *     return b.build();
 * }
 * }</pre>
 */
@SuppressWarnings("all")
public final class MappingEngineExample {

    private MappingEngineExample() { }

    /*
     * Real-world usage patterns based on the opt-cloud-dc codebase.
     *
     * Pattern 1 — Simple auto-copy:
     *   MappingEngine.copy(source, builder);
     *   Replaces: builder.setA(source.getA()); builder.setB(source.getB()); ...
     *
     * Pattern 2 — Auto-copy with renames:
     *   MappingEngine.copy(source, builder,
     *       FieldMapping.of("adminStatus", "adminState"),
     *       FieldMapping.of("operStatus", "operState"));
     *
     * Pattern 3 — Auto-copy with converter:
     *   MappingEngine.copy(source, builder,
     *       FieldMapping.of("protectionType", apiProtectionTypeToDev));
     *
     * Pattern 4 — Auto-copy excluding fields (to handle manually after):
     *   MappingEngine.copyExcluding(source, builder, "nestedObj", "enumField");
     *   // Then handle nestedObj and enumField manually
     *
     * Pattern 5 — Register enum converters globally at startup:
     *   MappingEngine.registerEnumConverter(ApiProtectionType.class, DevProtectionType.class);
     *   // Now all future copy() calls will auto-convert this enum type
     */
}
