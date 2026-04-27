/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.cmcc.util;

import org.json.JSONArray;
import org.json.JSONObject;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.MeLocations;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.MeLocationsBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.me.info.EqObject;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.me.info.EqObjectBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.me.info.EqObjectKey;
import org.opendaylight.yangtools.yang.common.Uint16;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 模拟子架图位置信息工具类
 *
 * @author Quan Jingyuan
 * @since 2020/9/18
 **/
public class OptelLocationUtil {

    public static MeLocations getLocations(int type, String productName) {

        int[] deviceType = {6801, 6802};
        if (type == deviceType[0] || type == deviceType[1]) {
            return getLocationInfoA1(productName);
        } else {
            return getLocationInfoS10();
        }
    }

    private static MeLocations getMeLocationsFromJson(String type, String productName) {
        MeLocationsBuilder meLocationsBuilder = new MeLocationsBuilder();
        List<EqObjectBuilder> eqObjectList = new ArrayList<>();
        String s = readJsonCofigs("/me-location.json");

        JSONObject object = new JSONObject(s).getJSONObject(type);
        JSONArray jsonArray = object.getJSONArray("eq-objects");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject item = jsonArray.getJSONObject(i);
            if (productName == null) {
                eqObjectList.add(getEqObjectBuilder(item));
                continue;
            }
            if ("".equals(item.getString("productName"))) {
                eqObjectList.add(getEqObjectBuilder(item));
            } else {
                boolean judge = item.getString("productName").contains(productName.substring(productName.lastIndexOf("-") + 1));
                if (judge) {
                    eqObjectList.add(getEqObjectBuilder(item));
                }
            }
        }
        Map<EqObjectKey, EqObject> cs = eqObjectList.stream().collect(Collectors.toMap(EqObjectBuilder::key, EqObjectBuilder::build));
        meLocationsBuilder.setEqObject(cs);
        meLocationsBuilder.setMeHeight(Uint16.valueOf(object.getInt("height")));
        meLocationsBuilder.setMeWidth(Uint16.valueOf(object.getInt("width")));
        return meLocationsBuilder.build();
    }

    private static EqObjectBuilder getEqObjectBuilder(JSONObject item) {
        EqObjectBuilder eqInfoBuilder = new EqObjectBuilder();
        eqInfoBuilder.withKey(new EqObjectKey(Uint16.valueOf(item.getInt("slot"))));
        eqInfoBuilder.setSlot(Uint16.valueOf(item.getInt("slot")));
        eqInfoBuilder.setEqHeight(Uint16.valueOf(item.getInt("eqHeight")));
        eqInfoBuilder.setEqWidth(Uint16.valueOf(item.getInt("eqWidth")));
        eqInfoBuilder.setLocationX(Uint16.valueOf(item.getInt("locationX")));
        eqInfoBuilder.setLocationY(Uint16.valueOf(item.getInt("locationY")));
        return eqInfoBuilder;
    }

    /**
     * 子架图位置数据模拟
     */
    public static MeLocations getLocationInfoS10() {
        return getMeLocationsFromJson("location-3u", null);
    }

    private static MeLocations getLocationInfoA1(String productName) {
        return getMeLocationsFromJson("location-1u", productName);
    }

    /*public static DevCapabilitySet getDevCapabilitySet(int type) {
        int[] deviceType = {6801, 6802};
        if (type == deviceType[0] || type == deviceType[1]) {
            return getLocationInfo1u();
        } else {
            return getLocationInfo3u();
        }
    }

    private static DevCapabilitySet getLocationInfo1u() {
        DevCapabilitySetBuilder devCapabilitySetBuilder = new DevCapabilitySetBuilder();
        SlotCapabilityPacBuilder slotCapabilityPacBuilder = new SlotCapabilityPacBuilder();
        List<SlotCapabilityPacs> slotCapabilityPacs = new ArrayList<>();
        int x = 5, y = 5;
        int length = 50;
        SlotCapabilityPacsBuilder eqObjectList = new SlotCapabilityPacsBuilder();

        eqObjectList.setName("Fan(1)");
        eqObjectList.withKey(new SlotCapabilityPacsKey(Uint16.valueOf(1)));
        eqObjectList.setSlotNo(Uint16.valueOf(1));
        eqObjectList.setEqType(EqType.Fan);
        eqObjectList.setHeight(Uint16.valueOf((int) (length * 1.5)));
        eqObjectList.setWidth(Uint16.valueOf(length * 3));
        eqObjectList.setPosX(Uint16.valueOf(x));
        eqObjectList.setPos(Uint16.valueOf(y));
        slotCapabilityPacs.add(eqObjectList.build());
        slotCapabilityPacBuilder.setSlotCapabilityPacs(slotCapabilityPacs);
        devCapabilitySetBuilder.setSlotCapabilityPac(slotCapabilityPacBuilder.build());
        ShelfInfoBuilder shelfInfoBuilder = new ShelfInfoBuilder();
        shelfInfoBuilder.setHeight(Uint16.valueOf((int) (length * 1.5) + y * 2));
        shelfInfoBuilder.setWidth(Uint16.valueOf(length * 3 + x * 2));
        devCapabilitySetBuilder.setShelfInfo(shelfInfoBuilder.build());
        return devCapabilitySetBuilder.build();
    }

    public static DevCapabilitySet getLocationInfo3u() {
        DevCapabilitySetBuilder devCapabilitySetBuilder = new DevCapabilitySetBuilder();
        SlotCapabilityPacBuilder slotCapabilityPacBuilder = new SlotCapabilityPacBuilder();
        List<SlotCapabilityPacs> slotCapabilityPacs = new ArrayList<>();
        int num = 13;
        int interval = 2;
        int x = 5, y = 5;
        int length = 50;
        int width = length - 15;
        for (int i = 0; i < num; i++) {
            SlotCapabilityPacsBuilder pacsBuilder = new SlotCapabilityPacsBuilder();
            pacsBuilder.withKey(new SlotCapabilityPacsKey(Uint16.valueOf(i)));
            pacsBuilder.setSlotNo(Uint16.valueOf(i + 1));
            switch (i) {
                case 0:
                    pacsBuilder.withKey(new SlotCapabilityPacsKey(Uint16.valueOf(11)));
                    pacsBuilder.setSlotNo(Uint16.valueOf(11));
                    pacsBuilder.setEqType(EqType.Service);
                    pacsBuilder.setHeight(Uint16.valueOf(((length - interval) / 2 * 3 + 2 * interval) * 2 + interval));
                    pacsBuilder.setWidth(Uint16.valueOf(width));
                    pacsBuilder.setPosX(Uint16.valueOf(x));
                    pacsBuilder.setPos(Uint16.valueOf(y));
                    break;
                case 1:
                    pacsBuilder.withKey(new SlotCapabilityPacsKey(Uint16.valueOf(12)));
                    pacsBuilder.setSlotNo(12);
                    pacsBuilder.setEqType(EqType.Clock);
                    pacsBuilder.setHeight((length - interval) / 2 * 3 + 2 * interval);
                    pacsBuilder.setWidth(width);
                    pacsBuilder.setPosX(x + width + interval);
                    pacsBuilder.setPos(y);
                    break;
                case 2:
                    pacsBuilder.withKey(new SlotCapabilityPacsKey(13));
                    pacsBuilder.setSlotNo(13);
                    pacsBuilder.setEqType(EqType.CrossConnect);
                    pacsBuilder.setHeight((length - interval) / 2 * 3 + 2 * interval);
                    pacsBuilder.setWidth(width);
                    pacsBuilder.setPosX(x + width + interval);
                    pacsBuilder.setPos(y + (length - interval) / 2 * 3 + 2 * interval + interval);
                    break;
                case 3:
                    pacsBuilder.withKey(new SlotCapabilityPacsKey(1));
                    pacsBuilder.setSlotNo(1);
                    pacsBuilder.setEqType(EqType.Fan);
                    pacsBuilder.setHeight((length - interval) / 2);
                    pacsBuilder.setWidth(length * 3 + width);
                    pacsBuilder.setPosX(x + width * 2 + interval * 2);
                    pacsBuilder.setPos(y);
                    break;
                case 4:
                    pacsBuilder.withKey(new SlotCapabilityPacsKey(2));
                    pacsBuilder.setSlotNo(2);
                    pacsBuilder.setEqType(EqType.Power);
                    pacsBuilder.setHeight((length - interval) / 2);
                    pacsBuilder.setWidth(length * 3 + width);
                    pacsBuilder.setPosX(x + width * 3 + interval * 3 + length * 3);
                    pacsBuilder.setPos(y);
                    pacsBuilder.setName(null);
                    break;
                case 5:
                    pacsBuilder.withKey(new SlotCapabilityPacsKey(3));
                    pacsBuilder.setSlotNo(3);
                    pacsBuilder.setEqType(EqType.Power);
                    pacsBuilder.setHeight((length - interval) / 2);
                    pacsBuilder.setWidth(length * 3 + width);
                    pacsBuilder.setPosX(x + width * 2 + interval * 2);
                    pacsBuilder.setPos(y + (length - interval) / 2 + interval);
                    pacsBuilder.setName(null);
                    break;
                case 6:
                    pacsBuilder.withKey(new SlotCapabilityPacsKey(4));
                    pacsBuilder.setSlotNo(4);
                    pacsBuilder.setEqType(EqType.Power);
                    pacsBuilder.setHeight((length - interval) / 2);
                    pacsBuilder.setWidth(length * 3 + width);
                    pacsBuilder.setPosX(x + width * 3 + interval * 3 + length * 3);
                    pacsBuilder.setPos(y + (length - interval) / 2 + interval);
                    pacsBuilder.setName(null);
                    break;
                case 7:
                    pacsBuilder.withKey(new SlotCapabilityPacsKey(9));
                    pacsBuilder.setSlotNo(9);
                    pacsBuilder.setName("ETH(4)");
                    pacsBuilder.setEqType(EqType.Power);
                    pacsBuilder.setHeight((length - interval) / 2);
                    pacsBuilder.setWidth((length * 3 + width) * 2 + interval);
                    pacsBuilder.setPosX(x + width * 2 + interval * 2);
                    pacsBuilder.setPos(y + length + interval);
                    break;
                case 8:
                    pacsBuilder.withKey(new SlotCapabilityPacsKey(10));
                    pacsBuilder.setSlotNo(10);
                    pacsBuilder.setEqType(EqType.Power);
                    pacsBuilder.setHeight((length - interval) / 2);
                    pacsBuilder.setWidth((length * 3 + width) * 2 + interval);
                    pacsBuilder.setPosX(x + width * 2 + interval * 2);
                    pacsBuilder.setPos(y + length + interval + (length - interval) / 2 + interval);
                    pacsBuilder.setName(null);
                    break;
                case 9:
                    pacsBuilder.withKey(new SlotCapabilityPacsKey(5));
                    pacsBuilder.setSlotNo(5);
                    pacsBuilder.setEqType(EqType.Power);
                    pacsBuilder.setHeight((length - interval) / 2);
                    pacsBuilder.setWidth(length * 3 + width);
                    pacsBuilder.setPosX(x + width * 2 + interval * 2);
                    pacsBuilder.setPos(y + length * 2 + interval * 2);
                    break;
                case 10:
                    pacsBuilder.withKey(new SlotCapabilityPacsKey(6));
                    pacsBuilder.setSlotNo(6);
                    pacsBuilder.setEqType(EqType.Power);
                    pacsBuilder.setHeight((length - interval) / 2);
                    pacsBuilder.setWidth(length * 3 + width);
                    pacsBuilder.setPosX(x + width * 3 + interval * 3 + length * 3);
                    pacsBuilder.setPos(y + length * 2 + interval * 2);
                    break;
                case 11:
                    pacsBuilder.withKey(new SlotCapabilityPacsKey(7));
                    pacsBuilder.setSlotNo(7);
                    pacsBuilder.setName("E1(2)");
                    pacsBuilder.setEqType(EqType.Power);
                    pacsBuilder.setHeight((length - interval) / 2);
                    pacsBuilder.setWidth(length * 3 + width);
                    pacsBuilder.setPosX(x + width * 2 + interval * 2);
                    pacsBuilder.setPos(y + length * 2 + interval * 2 + (length - interval) / 2 + interval);

                    break;
                case 12:
                    pacsBuilder.withKey(new SlotCapabilityPacsKey(8));
                    pacsBuilder.setSlotNo(8);
                    pacsBuilder.setEqType(EqType.Power);
                    pacsBuilder.setHeight((length - interval) / 2);
                    pacsBuilder.setWidth(length * 3 + width);
                    pacsBuilder.setPosX(x + width * 3 + interval * 3 + length * 3);
                    pacsBuilder.setPos(y + length * 2 + interval * 2 + (length - interval) / 2 + interval);
                    pacsBuilder.setName(null);
                    break;
                default:
                    break;
            }
            slotCapabilityPacs.add(pacsBuilder.build());
        }
        ShelfInfoBuilder shelfInfoBuilder = new ShelfInfoBuilder();
        shelfInfoBuilder.setHeight(length * 3 + interval * 2 + y * 2);
        shelfInfoBuilder.setWidth(width * 2 + (length * 3 + width) * 2 + y * 2 + interval * 4);
        slotCapabilityPacBuilder.setSlotCapabilityPacs(slotCapabilityPacs);
        devCapabilitySetBuilder.setShelfInfo(shelfInfoBuilder.build());
        devCapabilitySetBuilder.setSlotCapabilityPac(slotCapabilityPacBuilder.build());
        return devCapabilitySetBuilder.build();
    }*/

    private static String readJsonCofigs(String path) {
        BufferedReader bufferedReader;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStream inputStream = OptelLocationUtil.class.getResourceAsStream(path);
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            int data;
            while ((data = bufferedReader.read()) != -1) {
                stringBuilder.append((char) data);
            }
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


}
