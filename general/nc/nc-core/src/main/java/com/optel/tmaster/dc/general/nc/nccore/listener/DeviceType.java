package com.optel.tmaster.dc.general.nc.nccore.listener;

import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 设备类型
 *
 * @author Quan Jingyuan
 * @since 2022/9/13
 **/
@SuppressWarnings("all")
public enum DeviceType {

    /**
     * WDM设备
     */
    OTN(6, ""),

    /**
     * WDM设备
     */
    WDM(7, ""),

    /**
     * DCI设备
     */
    CTC_DCI(201, Paths.get("schema", "dci", "diligent").toString()),

    /**
     * 极简OTN
     */
    CMCC_DCI(202, Paths.get("schema", "dci", "yearning").toString()),

    /**
     * 移动
     */
    CMCC_OTN(1, Paths.get("schema", "s-otn", "yearning").toString()),

    /**
     * 电信
     */
    CTC_OTN(2, Paths.get("schema", "s-otn", "diligent").toString()),

    /**
     * 电信 兼容老版本设备
     */
    CTC_OTN_OLD(3, Paths.get("schema", "s-otn", "diligent").toString()),

    /**
     * 标准移动OTN
     */
    STANDARD_CMCC_OTN(10001, Paths.get("schema", "standard-s-otn", "yearning").toString()),

    /**
     * 标准电信OTN
     **/
    STANDARD_CTC_OTN(10002, Paths.get("schema", "standard-s-otn", "diligent").toString()),

    /**
     * 标准DCI设备（电信）
     */
    STANDARD_CTC_DCI(10201, Paths.get("schema", "standard-dci", "diligent").toString()),

    /**
     * 标准极简OTN（移动）
     */
    STANDARD_CMCC_DCI(10202, Paths.get("schema", "standard-dci", "yearning").toString()),
    ;

    private final Integer type;
    private final String dir;

    DeviceType(Integer type, String dir) {
        this.type = type;
        this.dir = dir;
    }

    public Integer getType() {
        return type;
    }

    public String getDir() {
        return dir;
    }

    public static DeviceType getDeviceTypeByType(Integer type) {
        for (DeviceType deviceType : values()) {
            if (Objects.equals(deviceType.getType(), type)) {
                return deviceType;
            }
        }
        return null;
    }

    public static DeviceType getDeviceTypeByType(String type) {
        int typeTrans = Integer.parseInt(type);
        for (DeviceType deviceType : values()) {
            if (Objects.equals(deviceType.getType(), typeTrans)) {
                return deviceType;
            }
        }
        return null;
    }

    public static Integer getYangModeByDir(String dir) {
        if (dir != null && !dir.isEmpty()) {
            for (DeviceType deviceType : values()) {
                if (Objects.equals(deviceType.getDir(), dir)) {
                    return deviceType.getType();
                }
            }
        }
        return null;
    }

    public static List<String> getSchemaCacheDirectory() {
        return Stream.of(values()).map(DeviceType::getDir).collect(Collectors.toList());
    }

}
