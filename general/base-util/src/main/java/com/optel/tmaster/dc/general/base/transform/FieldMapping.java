/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.base.transform;

import java.util.Objects;

/**
 * Declarative field mapping: source field name → target field name, with optional type converter.
 */
public final class FieldMapping {

    private final String sourceField;
    private final String targetField;
    private final ObjectConverter<Object, Object> converter;

    private FieldMapping(String sourceField, String targetField, ObjectConverter<Object, Object> converter) {
        this.sourceField = sourceField;
        this.targetField = targetField;
        this.converter = converter;
    }

    public static FieldMapping of(String sourceField, String targetField) {
        return new FieldMapping(sourceField, targetField, null);
    }

    /** Same-name mapping with converter. */
    public static FieldMapping of(String field, ObjectConverter<?, ?> converter) {
        return new FieldMapping(field, field, cast(converter));
    }

    /** Different-name mapping with converter. */
    public static FieldMapping of(String sourceField, String targetField, ObjectConverter<?, ?> converter) {
        return new FieldMapping(sourceField, targetField, cast(converter));
    }

    public String sourceField() { return sourceField; }
    public String targetField() { return targetField; }
    public ObjectConverter<Object, Object> converter() { return converter; }

    @SuppressWarnings("unchecked")
    private static ObjectConverter<Object, Object> cast(ObjectConverter<?, ?> c) {
        return (ObjectConverter<Object, Object>) c;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FieldMapping that)) return false;
        return sourceField.equals(that.sourceField)
            && targetField.equals(that.targetField)
            && Objects.equals(converter, that.converter);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceField, targetField, converter);
    }

    @Override
    public String toString() {
        return sourceField + "→" + targetField + (converter != null ? " (converted)" : "");
    }
}
