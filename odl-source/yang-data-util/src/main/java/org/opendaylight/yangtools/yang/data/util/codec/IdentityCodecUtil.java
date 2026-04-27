/*
 * Copyright (c) 2018 Pantheon Technologies, s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.yangtools.yang.data.util.codec;

import static com.google.common.base.Preconditions.checkState;

import com.google.common.annotations.Beta;
import java.util.function.Function;
import org.eclipse.jdt.annotation.NonNullByDefault;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.common.QNameModule;
import org.opendaylight.yangtools.yang.model.api.EffectiveModelContext;
import org.opendaylight.yangtools.yang.model.api.IdentitySchemaNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility methods for implementing string-to-identity codecs.
 *
 * @author Robert Varga
 */
@Beta
@NonNullByDefault
public final class IdentityCodecUtil {
    private static final Logger LOG = LoggerFactory.getLogger(IdentityCodecUtil.class);
    private IdentityCodecUtil() {
        // Hidden on purpose
    }

    /**
     * Parse a string into a QName using specified prefix-to-QNameModule mapping function, interpreting the result
     * as an IdentitySchemaNode existing in specified SchemaContext.
     *
     * @param value string value to parse
     * @param schemaContext Parent schema context
     * @param prefixToModule prefix-to-QNameModule mapping function
     * @return Corresponding IdentitySchemaNode.
     * @throws IllegalArgumentException if the value is invalid or does not refer to an existing identity
     */
    public static IdentitySchemaNode parseIdentity(final String value, final EffectiveModelContext schemaContext,
            final Function<String, QNameModule> prefixToModule) {
        final String identityStr;
        if ("".equals(value) || value == null) {
            //dc兼容处理 identity 报空标签
            identityStr = "ssssss";
        }else{
            identityStr=value;
        }
        final QName qname = QNameCodecUtil.decodeQName(identityStr, prefixToModule);
        final var optModule = schemaContext.findModule(qname.getModule());
        checkState(optModule.isPresent(), "Parsed QName %s refers to a non-existent module", qname);
        //找不到 identity 赋值UNKNOWN ,找不到UNKNOWN赋值最后一个
        IdentitySchemaNode defaultIdentity = null;

        for (var identity : optModule.orElseThrow().getIdentities()) {
            //为null初始化
            if (defaultIdentity == null) {
                defaultIdentity = identity;
            }
            // 不为 UNKNOWN 时 每次循环赋值
            if (!defaultIdentity.getQName().getLocalName().toUpperCase().contains("UNKNOWN")) {
                defaultIdentity = identity;
            }
            if (qname.equals(identity.getQName())) {
                return identity;
            }
        }
        //dc兼容处理 找不到identity
        LOG.error("Parsed QName " + qname + " does not refer to a valid identity", new IllegalArgumentException("Parsed QName " + qname + " does not refer to a valid identity"));
//        throw new IllegalArgumentException("Parsed QName " + qname + " does not refer to a valid identity");
        return defaultIdentity;
    }
}
