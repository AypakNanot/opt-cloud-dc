/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.base.util;

import cn.hutool.core.io.file.PathUtil;
import cn.hutool.core.util.StrUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 文件工具类
 *
 * @author dzm
 * @since 2024/8/22
 */
public class DcFileUtil {

    private static final Logger LOG = LoggerFactory.getLogger(DcFileUtil.class);

    private DcFileUtil() {}

    /**
     * 删除设备yang模型临时文件夹
     *
     * @param id 设备连接ID
     */
    public static void deleteCacheTemp(String id) {
        try {
            Path path = StrUtil.isEmpty(id) ? Paths.get(GlobalConfig.getKarafHome(), "cache", "temp")
                    : Paths.get(GlobalConfig.getKarafHome(), "cache", "temp", id);
            PathUtil.del(path);
            LOG.info("Delete the directory: {} successfully", path);
        } catch (Exception e) {
            LOG.error("Delete the temp cache directory unsuccessfully, id: {}", id, e);
        }
    }

}
