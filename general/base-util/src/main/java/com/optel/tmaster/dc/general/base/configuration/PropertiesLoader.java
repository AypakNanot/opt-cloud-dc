/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.base.configuration;

import com.optel.tmaster.dc.general.base.util.GlobalConfig;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

/**
 * <pre>
 *    o o o o o o     p p p p p p   t t t t t t t    e e e e e e    l l
 *    o o     o o     p p       p        t t         e e            l l
 *    o o     o o     p p       p        t t         e e            l l
 *    o o     o o     p p       p        t t         e e            l l
 *    o o     o o     p p       p        t t         e e            l l
 *    o o     o o     p p p p p p        t t         e e e e e e    l l
 *    o o     o o     p p                t t         e e            l l
 *    o o     o o     p p                t t         e e            l l
 *    o o     o o     p p                t t         e e            l l
 *    o o o o o o     p p                t t         e e e e e e    l l l l l l
 *
 *
 *          l       i    h     h    u     u       a a
 *          l            h     h    u     u     a     a
 *          l       i    h h h h    u     u     a     a
 *          l       i    h     h    u     u     a     a
 *          l l l   i    h     h      u u  u      a a   a
 *
 *              LiHua       佛主保佑       永无bBUG
 * </pre>
 * 2019/9/26 17:02:31
 *
 * @author LiH
 * @version V1.0.0
 */
public class PropertiesLoader {

    private static Logger LOG = LoggerFactory.getLogger("PropertiesLoader");

    /**
     * 加载配置文件
     * @param configuration 配置对象
     * @param isCache 是否缓存
     */
    public static void loadProperties(final IConfiguration configuration, boolean isCache){
        if(configuration instanceof IPropertiesConfiguration){
            if(configuration instanceof  ISystemConfiguration){
                loadSysConfig((IPropertiesConfiguration) configuration);
            }else{
                loadResourcesProperties((IPropertiesConfiguration)configuration,isCache);
            }
        }
    }

    /**
     * 配置文件加载类
     * @param config 配置对象
     * @param isCache 是否需要缓存
     */
    private static void loadResourcesProperties(final IPropertiesConfiguration config, final boolean isCache) {
        Bundle bundle = FrameworkUtil.getBundle(config.getClass());
        URL resource = bundle.getResource(config.getRelativizePath());
        LOG.info("loader properties path :{},Protocol:{}",resource.getFile(),resource.getProtocol());
        String prefix = config.getPrefix();
        if(prefix == null || "".equals(prefix)){
            loadNoPrefixProperties(config,resource);
        }else{
            loadPrefixProperties(config,resource);
        }
        if(isCache){
            ConfigurationMgr.addAndUpdate(config);
        }
    }

    /**
     * 加载系统配置文件
     * @param config 配置文件
     */
    private static void loadSysConfig(final IPropertiesConfiguration config) {
        final String karafHome = GlobalConfig.getKarafHome();
        try{
            String relativizePath = config.getRelativizePath();
            Path path = Paths.get(karafHome).resolve(relativizePath);
            LOG.info("Load config file path:{}",path.toAbsolutePath());
            URL resource = path.toUri().toURL();
            String prefix = config.getPrefix();
            if(prefix == null || "".equals(prefix)){
                loadNoPrefixProperties(config,resource);
            }else{
                loadPrefixProperties(config,resource);
            }
        }catch(Exception ex){
            LOG.error("Load configuration error. ClassName:{}",config.getRelativizePath(),ex);
        }
        ConfigurationMgr.addAndUpdate(config);
    }

    /**
     * 加载有前缀的配置文件
     * @param config 配置
     * @param resource 路径
     */
    private static void loadPrefixProperties(IPropertiesConfiguration config, URL resource) {
        try {
            PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration();
            propertiesConfiguration.setEncoding(GlobalConfig.getSystemEncoding());
            propertiesConfiguration.load(resource);
            propertiesConfiguration.setListDelimiter(config.getDelimiter());
            Iterator<String> keys = propertiesConfiguration.getKeys(config.getPrefix());
            while (keys.hasNext()) {
                String key = keys.next();
                String v = propertiesConfiguration.getString(key);
                int index = config.getPrefix().length();
                String k = key.substring((index == 0 ? -1 : index) +1);
                config.setProperty(k,v);
                LOG.info("Configuration, key:{},value:{}",k,v);
            }
        } catch (ConfigurationException e) {
            LOG.error("Load Configuration file:{} is error.",config.getRelativizePath(),e);
        }
    }

    /**
     * 加载没有前缀的配置文件
     * @param config 配置
     * @param resource 路径
     */
    private static void loadNoPrefixProperties(IPropertiesConfiguration config, URL resource) {
        Properties prop = new Properties();
        try(InputStream is = resource.openStream();
            InputStreamReader isr = new InputStreamReader(is, GlobalConfig.getSystemEncoding())){
            prop.load(isr);
        } catch (IOException e) {
            LOG.error("loadNoPrefixProperties file:{} is error.",config.getRelativizePath(),e);
        }
        for (Object o : prop.keySet()) {
            String key = String.valueOf(o);
            String value = prop.getProperty(key);
            config.setProperty(key, value);
        }
    }

    /**
     * 加载临时资源文件
     * @param clz 需要使用临时资源文件的class
     * @param path 相对bundle中resources路径
     * @return 数据
     */
    protected static Properties loadProperties(Class<?> clz,String path) {
        Bundle bundle = FrameworkUtil.getBundle(clz);
        URL resource = bundle.getResource(path);
        Properties properties = new Properties();
        try(InputStream is = resource.openStream()){
            properties.load(is);
        } catch (IOException e) {
            LOG.error("Load properties error. file:{}",path,e);
        }
        return properties;
    }
}
