/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.nc.nccore.common;

import java.io.Serializable;
import java.util.List;

/**
 * opt 支持的设备版本对应实体类
 * @author Quan Jingyuan
 * @since 2024/6/18
 **/
public class OptVersion implements Serializable {
    /**
    * yangMode
    * */
   private String yangMode;
   /**
   * 所属YangMode的命名空间+revision
   * */
    private List<String> capability;

    public String getYangMode() {
        return yangMode;
    }

    public void setYangMode(String yangMode) {
        this.yangMode = yangMode;
    }

    public List<String> getCapability() {
        return capability;
    }

    public void setCapability(List<String> capability) {
        this.capability = capability;
    }
}
