/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.util;

import com.optel.tmaster.dc.device.impl.base.transform.ITransform;
import org.json.JSONArray;
import org.json.JSONObject;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.MeLocations;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.me.info.EqObject;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.me.info.EqObjectBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.me.info.EqObjectKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.DevCapabilitySet;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.DevCapabilitySetBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.dev.capability.set.ShelfInfoBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.dev.capability.set.SlotCapabilityPacBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.slot.capability.pac.list.SlotCapabilityPacs;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.slot.capability.pac.list.SlotCapabilityPacsBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.slot.capability.pac.list.SlotCapabilityPacsKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.EqType;
import org.opendaylight.yangtools.yang.common.Uint16;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 模拟子架图位置信息工具类
 *
 * @author Quan Jingyuan
 * @since 2020/9/18
 **/
public class OptelLocationUtil implements ITransform {

    public static MeLocations getLocations(int type, String productName, Boolean powerType ) {

        int[] deviceType = {6805,6806};
        if (type == deviceType[0]) {
            return getLocationInfoA1(productName);
        } else {
            //电信只能通过productName 区分 3u 和 5u
            String meForFive="M5B";
            if(type==deviceType[1]&&productName.contains(meForFive)){
                //如果是交流电
                if(powerType){
                    return getLocationInfo5U("ac");
                }else {
                    return getLocationInfo5U("dc");
                }

            }
            //如果是交流电
            if(powerType){
                return getLocationInfoS10("ac");
            }else {
                return getLocationInfoS10("dc");
            }
        }
    }
    private static  MeLocations getMeLocationsFromJson(String type,String productName){
        org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.MeLocationsBuilder meLocationsBuilder = new org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.MeLocationsBuilder();
        List<EqObject> eqObjectList = new ArrayList<>();
        String s=readJsonCofigs("/me-location.json");

        JSONObject object=new JSONObject(s).getJSONObject(type);
        JSONArray jsonArray=object.getJSONArray("eq-objects");
        for(int i=0;i<jsonArray.length();i++){
            JSONObject item=jsonArray.getJSONObject(i);
            if(productName==null){
                eqObjectList.add(getEqObjectBuilder(item).build());
                continue;
            }

            if("".equals(item.getString("productName"))){
                if(item.getString("exceptProductName").contains(productName.substring(productName.lastIndexOf("-")+1))){
                    continue;
                }
                eqObjectList.add(getEqObjectBuilder(item).build());
            }else{
                boolean judge= item.getString("productName").contains(productName.substring(productName.lastIndexOf("-")+1));
                if(judge){
                    eqObjectList.add(getEqObjectBuilder(item).build());
                }
            }
        }
        Map<EqObjectKey, EqObject> cs = eqObjectList.stream().collect(Collectors.toMap(EqObject::key, Function.identity()));
        meLocationsBuilder.setEqObject(cs);
        meLocationsBuilder.setMeHeight(Uint16.valueOf(object.getInt("height")));
        meLocationsBuilder.setMeWidth(Uint16.valueOf(object.getInt("width")));
        return meLocationsBuilder.build();
    }
    private static org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.me.info.EqObjectBuilder getEqObjectBuilder(JSONObject item){
        org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.me.info.EqObjectBuilder eqInfoBuilder = new EqObjectBuilder();
        eqInfoBuilder.withKey(new EqObjectKey(Uint16.valueOf(item.getInt("slot"))));
        eqInfoBuilder.setSlot(Uint16.valueOf(item.getInt("slot")));
        eqInfoBuilder.setEqHeight(Uint16.valueOf(item.getInt("eqHeight")));
        eqInfoBuilder.setEqWidth(Uint16.valueOf(item.getInt("eqWidth")));
        eqInfoBuilder.setLocationX(Uint16.valueOf(item.getInt("locationX")));
        eqInfoBuilder.setLocationY(Uint16.valueOf(item.getInt("locationY")));
        return  eqInfoBuilder;
    }
    /**
     * 子架图位置数据模拟
     */
    public static MeLocations getLocationInfoS10(String powerType) {
        return getMeLocationsFromJson("location-3u-"+powerType,null);
    }
    /**
     * 子架图位置数据模拟
     */
    public static MeLocations getLocationInfo5U(String powerType) {
        return getMeLocationsFromJson("location-5u-"+powerType,null);
    }
    private static MeLocations getLocationInfoA1(String productName) {
        return getMeLocationsFromJson("location-1u",productName);
    }

    public static DevCapabilitySet getDevCapabilitySet(int type) {
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
        slotCapabilityPacBuilder.setSlotCapabilityPacs(slotCapabilityPacs.stream().collect(Collectors.toMap(SlotCapabilityPacs::key, Function.identity())));
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
                    pacsBuilder.setSlotNo(Uint16.valueOf(12));
                    pacsBuilder.setEqType(EqType.Clock);
                    pacsBuilder.setHeight(Uint16.valueOf((length - interval) / 2 * 3 + 2 * interval));
                    pacsBuilder.setWidth(Uint16.valueOf(width));
                    pacsBuilder.setPosX(Uint16.valueOf(x + width + interval));
                    pacsBuilder.setPos(Uint16.valueOf(y));
                    break;
                case 2:
                    pacsBuilder.withKey(new SlotCapabilityPacsKey(Uint16.valueOf(13)));
                    pacsBuilder.setSlotNo(Uint16.valueOf(13));
                    pacsBuilder.setEqType(EqType.CrossConnect);
                    pacsBuilder.setHeight(Uint16.valueOf((length - interval) / 2 * 3 + 2 * interval));
                    pacsBuilder.setWidth(Uint16.valueOf(width));
                    pacsBuilder.setPosX(Uint16.valueOf(x + width + interval));
                    pacsBuilder.setPos(Uint16.valueOf(y + (length - interval) / 2 * 3 + 2 * interval + interval));
                    break;
                case 3:
                    pacsBuilder.withKey(new SlotCapabilityPacsKey(Uint16.valueOf(1)));
                    pacsBuilder.setSlotNo(Uint16.valueOf(1));
                    pacsBuilder.setEqType(EqType.Fan);
                    pacsBuilder.setHeight(Uint16.valueOf((length - interval) / 2));
                    pacsBuilder.setWidth(Uint16.valueOf(length * 3 + width));
                    pacsBuilder.setPosX(Uint16.valueOf(x + width * 2 + interval * 2));
                    pacsBuilder.setPos(Uint16.valueOf(y));
                    break;
                case 4:
                    pacsBuilder.withKey(new SlotCapabilityPacsKey(Uint16.valueOf(2)));
                    pacsBuilder.setSlotNo(Uint16.valueOf(2));
                    pacsBuilder.setEqType(EqType.Power);
                    pacsBuilder.setHeight(Uint16.valueOf((length - interval) / 2));
                    pacsBuilder.setWidth(Uint16.valueOf(length * 3 + width));
                    pacsBuilder.setPosX(Uint16.valueOf(x + width * 3 + interval * 3 + length * 3));
                    pacsBuilder.setPos(Uint16.valueOf(y));
                    pacsBuilder.setName(null);
                    break;
                case 5:
                    pacsBuilder.withKey(new SlotCapabilityPacsKey(Uint16.valueOf(3)));
                    pacsBuilder.setSlotNo(Uint16.valueOf(3));
                    pacsBuilder.setEqType(EqType.Power);
                    pacsBuilder.setHeight(Uint16.valueOf((length - interval) / 2));
                    pacsBuilder.setWidth(Uint16.valueOf(length * 3 + width));
                    pacsBuilder.setPosX(Uint16.valueOf(x + width * 2 + interval * 2));
                    pacsBuilder.setPos(Uint16.valueOf(y + (length - interval) / 2 + interval));
                    pacsBuilder.setName(null);
                    break;
                case 6:
                    pacsBuilder.withKey(new SlotCapabilityPacsKey(Uint16.valueOf(4)));
                    pacsBuilder.setSlotNo(Uint16.valueOf(4));
                    pacsBuilder.setEqType(EqType.Power);
                    pacsBuilder.setHeight(Uint16.valueOf((length - interval) / 2));
                    pacsBuilder.setWidth(Uint16.valueOf(length * 3 + width));
                    pacsBuilder.setPosX(Uint16.valueOf(x + width * 3 + interval * 3 + length * 3));
                    pacsBuilder.setPos(Uint16.valueOf(y + (length - interval) / 2 + interval));
                    pacsBuilder.setName(null);
                    break;
                case 7:
                    pacsBuilder.withKey(new SlotCapabilityPacsKey(Uint16.valueOf(9)));
                    pacsBuilder.setSlotNo(Uint16.valueOf(9));
                    pacsBuilder.setName("ETH(4)");
                    pacsBuilder.setEqType(EqType.Power);
                    pacsBuilder.setHeight(Uint16.valueOf((length - interval) / 2));
                    pacsBuilder.setWidth(Uint16.valueOf((length * 3 + width) * 2 + interval));
                    pacsBuilder.setPosX(Uint16.valueOf(x + width * 2 + interval * 2));
                    pacsBuilder.setPos(Uint16.valueOf(y + length + interval));
                    break;
                case 8:
                    pacsBuilder.withKey(new SlotCapabilityPacsKey(Uint16.valueOf(10)));
                    pacsBuilder.setSlotNo(Uint16.valueOf(10));
                    pacsBuilder.setEqType(EqType.Power);
                    pacsBuilder.setHeight(Uint16.valueOf((length - interval) / 2));
                    pacsBuilder.setWidth(Uint16.valueOf((length * 3 + width) * 2 + interval));
                    pacsBuilder.setPosX(Uint16.valueOf(x + width * 2 + interval * 2));
                    pacsBuilder.setPos(Uint16.valueOf(y + length + interval + (length - interval) / 2 + interval));
                    pacsBuilder.setName(null);
                    break;
                case 9:
                    pacsBuilder.withKey(new SlotCapabilityPacsKey(Uint16.valueOf(5)));
                    pacsBuilder.setSlotNo(Uint16.valueOf(5));
                    pacsBuilder.setEqType(EqType.Power);
                    pacsBuilder.setHeight(Uint16.valueOf((length - interval) / 2));
                    pacsBuilder.setWidth(Uint16.valueOf(length * 3 + width));
                    pacsBuilder.setPosX(Uint16.valueOf(x + width * 2 + interval * 2));
                    pacsBuilder.setPos(Uint16.valueOf(y + length * 2 + interval * 2));
                    break;
                case 10:
                    pacsBuilder.withKey(new SlotCapabilityPacsKey(Uint16.valueOf(6)));
                    pacsBuilder.setSlotNo(Uint16.valueOf(6));
                    pacsBuilder.setEqType(EqType.Power);
                    pacsBuilder.setHeight(Uint16.valueOf((length - interval) / 2));
                    pacsBuilder.setWidth(Uint16.valueOf(length * 3 + width));
                    pacsBuilder.setPosX(Uint16.valueOf(x + width * 3 + interval * 3 + length * 3));
                    pacsBuilder.setPos(Uint16.valueOf(y + length * 2 + interval * 2));
                    break;
                case 11:
                    pacsBuilder.withKey(new SlotCapabilityPacsKey(Uint16.valueOf(7)));
                    pacsBuilder.setSlotNo(Uint16.valueOf(7));
                    pacsBuilder.setName("E1(2)");
                    pacsBuilder.setEqType(EqType.Power);
                    pacsBuilder.setHeight(Uint16.valueOf((length - interval) / 2));
                    pacsBuilder.setWidth(Uint16.valueOf(length * 3 + width));
                    pacsBuilder.setPosX(Uint16.valueOf(x + width * 2 + interval * 2));
                    pacsBuilder.setPos(Uint16.valueOf(y + length * 2 + interval * 2 + (length - interval) / 2 + interval));

                    break;
                case 12:
                    pacsBuilder.withKey(new SlotCapabilityPacsKey(Uint16.valueOf(8)));
                    pacsBuilder.setSlotNo(Uint16.valueOf(8));
                    pacsBuilder.setEqType(EqType.Power);
                    pacsBuilder.setHeight(Uint16.valueOf((length - interval) / 2));
                    pacsBuilder.setWidth(Uint16.valueOf(length * 3 + width));
                    pacsBuilder.setPosX(Uint16.valueOf(x + width * 3 + interval * 3 + length * 3));
                    pacsBuilder.setPos(Uint16.valueOf(y + length * 2 + interval * 2 + (length - interval) / 2 + interval));
                    pacsBuilder.setName(null);
                    break;
                default:
                    break;
            }
            slotCapabilityPacs.add(pacsBuilder.build());
        }
        ShelfInfoBuilder shelfInfoBuilder = new ShelfInfoBuilder();
        shelfInfoBuilder.setHeight(Uint16.valueOf(length * 3 + interval * 2 + y * 2));
        shelfInfoBuilder.setWidth(Uint16.valueOf(width * 2 + (length * 3 + width) * 2 + y * 2 + interval * 4));
        slotCapabilityPacBuilder.setSlotCapabilityPacs(slotCapabilityPacs.stream().collect(Collectors.toMap(SlotCapabilityPacs::key, Function.identity())));
        devCapabilitySetBuilder.setShelfInfo(shelfInfoBuilder.build());
        devCapabilitySetBuilder.setSlotCapabilityPac(slotCapabilityPacBuilder.build());
        return devCapabilitySetBuilder.build();
    }
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
