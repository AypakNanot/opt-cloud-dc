/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.base.transform;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Loads {@link MappingProfile} from JSON configuration files.
 *
 * <p>Supports loading from classpath or file system. Profiles are cached by path.</p>
 *
 * <h3>Config file example</h3>
 * <pre>{@code
 * {
 *   "name": "osu-connection-create",
 *   "sourceType": "com.optel.devconf.opt.connection.CreateOsuConnectionNeInput",
 *   "targetBuilder": "ccsa.yang.acc.osu.CreateOsuConnectionInputBuilder",
 *   "autoFields": ["connectionName", "frequency", "rate"],
 *   "renameFields": { "adminStatus": "adminState" },
 *   "skipFields": ["neId", "yangMode"]
 * }
 * }</pre>
 */
public final class MappingProfileLoader {

    private static final Logger LOG = LoggerFactory.getLogger(MappingProfileLoader.class);

    private static final Map<String, MappingProfile> CACHE = new ConcurrentHashMap<>();

    private MappingProfileLoader() { }

    /**
     * Load a single profile from classpath.
     */
    public static MappingProfile loadFromClasspath(String resourcePath) {
        return CACHE.computeIfAbsent("cp:" + resourcePath, k -> {
            String json = ResourceUtil.readStr(resourcePath, StandardCharsets.UTF_8);
            return parseProfile(json, resourcePath);
        });
    }

    /**
     * Load a single profile from filesystem.
     */
    public static MappingProfile loadFromFile(Path filePath) {
        return CACHE.computeIfAbsent("file:" + filePath, k -> {
            try {
                String json = Files.readString(filePath, StandardCharsets.UTF_8);
                return parseProfile(json, filePath.toString());
            } catch (IOException e) {
                throw new IllegalArgumentException("Failed to read mapping config: " + filePath, e);
            }
        });
    }

    /**
     * Load all profiles from a JSON array file on classpath.
     */
    public static List<MappingProfile> loadAllFromClasspath(String resourcePath) {
        String json = ResourceUtil.readStr(resourcePath, StandardCharsets.UTF_8);
        JSONArray array = JSONUtil.parseArray(json);
        List<MappingProfile> profiles = new ArrayList<>();
        for (int i = 0; i < array.size(); i++) {
            profiles.add(((JSONObject) array.get(i)).toBean(MappingProfile.class));
        }
        LOG.info("Loaded {} mapping profiles from {}", profiles.size(), resourcePath);
        for (var p : profiles) {
            CACHE.put("cp:" + resourcePath + "#" + p.getName(), p);
        }
        return Collections.unmodifiableList(profiles);
    }

    /**
     * Clear the cache (for testing).
     */
    public static void clearCache() {
        CACHE.clear();
    }

    private static MappingProfile parseProfile(String json, String source) {
        MappingProfile profile = JSONUtil.toBean(json, MappingProfile.class);
        LOG.info("Loaded mapping profile '{}' from {} (auto:{}, rename:{}, skip:{})",
            profile.getName(), source,
            profile.getAutoFields().size(), profile.getRenameFields().size(),
            profile.getSkipFields().size());
        return profile;
    }
}
