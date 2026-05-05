/*
 * Copyright © 2023 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.util;

import com.optel.tmaster.dc.general.base.util.RpcResultUtil;

import java.util.Objects;

/**
 * OtnUtils
 * 工具类
 * date       time        author
 * ─────────────────────────────
 * 2024/2/21   16:58      tanghaiqing
 * Copyright (c) 2024, H-OPTEL All Rights Reserved.
 *
 * @author tanghaiqing
 * @version V1.0.0
 */
public class OtnUtils {

    private OtnUtils () {}

    public static RpcResultUtil.OutRpcResult getOduConnectionOutRpcResult(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.create.odu.connection.output.ErrOrOk errOrOk) {
        RpcResultUtil.OutRpcResult result = new RpcResultUtil.OutRpcResult();
        if (Objects.isNull(errOrOk)) {
            result.setSuccess(false);
        } else {
            if (errOrOk.implementedInterface().equals(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.create.odu.connection.output.err.or.ok.OperErr.class)) {
                result.setSuccess(false);
                org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.create.odu.connection.output.err.or.ok.OperErr err = (org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.create.odu.connection.output.err.or.ok.OperErr) errOrOk;
                result.setMsg(err.getRpcError().getErrorMessage());
            } else {
                result.setSuccess(true);
            }
        }
        return result;
    }

    public static RpcResultUtil.OutRpcResult getEthConnectionOutRpcResult(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.create.eth.connection.output.ErrOrOk errOrOk) {
        RpcResultUtil.OutRpcResult result = new RpcResultUtil.OutRpcResult();
        if (Objects.isNull(errOrOk)) {
            result.setSuccess(false);
        } else {
            if (errOrOk.implementedInterface().equals(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.create.eth.connection.output.err.or.ok.OperErr.class)) {
                result.setSuccess(false);
                org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.create.eth.connection.output.err.or.ok.OperErr err = (org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.create.eth.connection.output.err.or.ok.OperErr) errOrOk;
                result.setMsg(err.getRpcError().getErrorMessage());
            } else {
                result.setSuccess(true);
            }
        }
        return result;
    }

    public static RpcResultUtil.OutRpcResult getEosConnectionOutRpcResult(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.create.eos.connection.output.ErrOrOk errOrOk) {
        RpcResultUtil.OutRpcResult result = new RpcResultUtil.OutRpcResult();
        if (Objects.isNull(errOrOk)) {
            result.setSuccess(false);
        } else {
            if (errOrOk.implementedInterface().equals(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.create.eos.connection.output.err.or.ok.CreateErr.class)) {
                result.setSuccess(false);
                org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.create.eos.connection.output.err.or.ok.CreateErr err = (org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.create.eos.connection.output.err.or.ok.CreateErr) errOrOk;
                result.setMsg(err.getRpcError().getErrorMessage());
            } else {
                result.setSuccess(true);
            }
        }
        return result;
    }

    public static RpcResultUtil.OutRpcResult getModifyVcgConnectionCapacityOutRpcResult(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.modify.vcg.connection.capacity.output.ErrOrOk errOrOk) {
        RpcResultUtil.OutRpcResult result = new RpcResultUtil.OutRpcResult();
        if (Objects.isNull(errOrOk)) {
            result.setSuccess(false);
        } else {
            if (errOrOk.implementedInterface().equals(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.modify.vcg.connection.capacity.output.err.or.ok.OperErr.class)) {
                result.setSuccess(false);
                org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.modify.vcg.connection.capacity.output.err.or.ok.OperErr err = (org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.modify.vcg.connection.capacity.output.err.or.ok.OperErr) errOrOk;
                result.setMsg(err.getRpcError().getErrorMessage());
            } else {
                result.setSuccess(true);
            }
        }
        return result;
    }

    public static RpcResultUtil.OutRpcResult getSdhConnectionOutRpcResult(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.create.sdh.connection.output.ErrOrOk errOrOk) {
        RpcResultUtil.OutRpcResult result = new RpcResultUtil.OutRpcResult();
        if (Objects.isNull(errOrOk)) {
            result.setSuccess(false);
        } else {
            if (errOrOk.implementedInterface().equals(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.create.sdh.connection.output.err.or.ok.OperErr.class)) {
                result.setSuccess(false);
                org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.create.sdh.connection.output.err.or.ok.OperErr err = (org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.create.sdh.connection.output.err.or.ok.OperErr) errOrOk;
                result.setMsg(err.getRpcError().getErrorMessage());
            } else {
                result.setSuccess(true);
            }
        }
        return result;
    }

    public static RpcResultUtil.OutRpcResult getEoOsuConnectionOutRpcResult(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.eo.osu.connection.output.ErrOrOk errOrOk) {
        RpcResultUtil.OutRpcResult result = new RpcResultUtil.OutRpcResult();
        if (Objects.isNull(errOrOk)) {
            result.setSuccess(false);
        } else {
            if (errOrOk.implementedInterface().equals(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.eo.osu.connection.output.err.or.ok.OperErr.class)) {
                result.setSuccess(false);
                org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.eo.osu.connection.output.err.or.ok.OperErr err = (org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.eo.osu.connection.output.err.or.ok.OperErr) errOrOk;
                result.setMsg(err.getRpcError().getErrorMessage());
            } else {
                result.setSuccess(true);
            }
        }
        return result;
    }

    public static RpcResultUtil.OutRpcResult getEos2eoosuConnectionOutRpcResult(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.eos2eoosu.connection.output.ErrOrOk errOrOk) {
        RpcResultUtil.OutRpcResult result = new RpcResultUtil.OutRpcResult();
        if (Objects.isNull(errOrOk)) {
            result.setSuccess(false);
        } else {
            if (errOrOk.implementedInterface().equals(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.eos2eoosu.connection.output.err.or.ok.OperErr.class)) {
                result.setSuccess(false);
                org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.eos2eoosu.connection.output.err.or.ok.OperErr err = (org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.eos2eoosu.connection.output.err.or.ok.OperErr) errOrOk;
                result.setMsg(err.getRpcError().getErrorMessage());
            } else {
                result.setSuccess(true);
            }
        }
        return result;
    }

    public static RpcResultUtil.OutRpcResult getOsuConnectionOutRpcResult(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.osu.connection.output.ErrOrOk errOrOk) {
        RpcResultUtil.OutRpcResult result = new RpcResultUtil.OutRpcResult();
        if (Objects.isNull(errOrOk)) {
            result.setSuccess(false);
        } else {
            if (errOrOk.implementedInterface().equals(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.osu.connection.output.err.or.ok.OperErr.class)) {
                result.setSuccess(false);
                org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.osu.connection.output.err.or.ok.OperErr err = (org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.osu.connection.output.err.or.ok.OperErr) errOrOk;
                result.setMsg(err.getRpcError().getErrorMessage());
            } else {
                result.setSuccess(true);
            }
        }
        return result;
    }
}
