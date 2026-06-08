/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.base.transform;

/**
 * Type converter for mapping between different types (e.g., API enum → Device enum).
 *
 * @param <S> source type
 * @param <T> target type
 */
@FunctionalInterface
public interface ObjectConverter<S, T> {

    T convert(S source);

    static <S, T> ObjectConverter<S, T> of(java.util.function.Function<S, T> fn) {
        return fn::apply;
    }
}
