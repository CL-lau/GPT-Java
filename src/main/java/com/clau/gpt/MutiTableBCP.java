package com.clau.gpt;

import com.dianping.frog.sdk.data.BinlogRawData;
import com.dianping.frog.sdk.data.ColumnInfo;
import com.dianping.frog.sdk.data.RawData;
import com.dianping.frog.sdk.runner.DefaultRuleRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MutiTableBCP extends DefaultRuleRunner {

    /**
     * 用来存储大于关系的字段
     * */
    private Map<String, String> greaterMap = new HashMap<>();

    /**
     * 用来存储小于关系的字段
     * */
    private Map<String, String> lessMap = new HashMap<>();

    /**
     * 用来存储大于关系的字段,表内部
     * */
    private Map<String, String> selfGreaterMap = new HashMap<>();

    /**
     * 用来存储小于关系的字段，表内部
     * */
    private Map<String, String> selfLessMap = new HashMap<>();

    /**
     * 用来存储大于关系的字段,外表内部
     * */
    private Map<String, String> outterGreaterMap = new HashMap<>();

    /**
     * 用来存储小于关系的字段，外表内部
     * */
    private Map<String, String> outterLessMap = new HashMap<>();

    /**
     * 用来存储相等关系的字段
     * */
    private Map<String, String> equalMap = new HashMap<>();

    /**
     * 用来存储可枚举关系的字段
     * */
    private Map<String, String> emunMap = new HashMap<>();

    /**
     * 后续可以增加其他的类型，例如：字段类型。
     * */

    {
        greaterMap.put("limit_first_order", "limit_first_order");
        greaterMap.put("origin_coupon_id", "id");
        greaterMap.put("origin_coupon_id", "activity_id");
        greaterMap.put("origin_activity_id", "activity_id");
        greaterMap.put("valid_end_time", "valid_end_time");

        lessMap.put("valid_start_time", "valid_start_time");

        selfGreaterMap.put("valid_start_time", "valid_start_time");
        selfGreaterMap.put("valid_end_time", "valid_end_time");

        outterGreaterMap.put("valid_start_time", "valid_start_time");
        outterGreaterMap.put("valid_end_time", "valid_end_time");

        equalMap.put("valid", "valid");
        equalMap.put("detail", "detail");
        equalMap.put("validity", "validity");
        equalMap.put("limit_price", "limit_price");
        equalMap.put("limit_times", "limit_times");
        equalMap.put("total_count", "total_count");
        equalMap.put("origin_coupon_id", "origin_coupon_id");
        equalMap.put("origin_activity_id", "origin_activity_id");

        emunMap.put("origin_coupon_id", "origin_activity_id");
        emunMap.put("origin_activity_id", "origin_coupon_id");
        emunMap.put("activity_id", "origin_activity_id");
        emunMap.put("valid", "valid");
    }

    private <T> T getFieldValue(BinlogRawData binlogRawData, String fieldName, Class<T> returnType) {
        ColumnInfo columnInfo = binlogRawData.getColumnInfoMap().get(fieldName);
        Object object = columnInfo.getNewValue();
        if (object != null && returnType != null) {
            return (T) object;
        }
        return null;
    }

    @Override
    public String check(RawData triggerData, RawData... targetData) throws Exception {
        BinlogRawData binlogTriggerData = (BinlogRawData) triggerData;
        BinlogRawData binlogTargetData = (BinlogRawData) targetData[0];

        String res = checkMutiTable(binlogTriggerData, binlogTargetData);

        if (Objects.isNull(res) && res.equals("")){
            return null;
        }else {
            return res;
        }
    }

    public String checkMutiTable(BinlogRawData binlogTriggerData, BinlogRawData binlogTargetData){
        StringBuilder res = new StringBuilder();
        res.append(checkGreater(binlogTriggerData, binlogTargetData));
        res.append(checkLess(binlogTriggerData, binlogTargetData));
        res.append(checkEqual(binlogTriggerData, binlogTargetData));
        res.append(checkEmun(binlogTriggerData, binlogTargetData));
        return res.toString();
    }

    public String checkGreater(BinlogRawData binlogTriggerData, BinlogRawData binlogTargetData){
        StringBuilder res = new StringBuilder();
        for (Map.Entry<String, String> greaterItem : this.greaterMap.entrySet()){
            Integer trigger = getFieldValue(binlogTriggerData, greaterItem.getKey(), Integer.class);
            Integer target = getFieldValue(binlogTargetData, greaterItem.getValue(), Integer.class);
            if (Objects.isNull(trigger) | Objects.isNull(target)){
                res.append("内字段 " + greaterItem.getKey() + "和外字段 " + greaterItem.getValue() + "存在null.");
            } else if(trigger <= target){
                res.append("内字段 " + greaterItem.getKey() + "和外字段 " + greaterItem.getValue() + "的大小关系不满足.");
            }

        }
        for (Map.Entry<String, String> greaterItem : this.selfGreaterMap.entrySet()){
            Integer selfTrigger = getFieldValue(binlogTriggerData, greaterItem.getKey(), Integer.class);
            Integer selfTarget = getFieldValue(binlogTriggerData, greaterItem.getValue(), Integer.class);
            if (Objects.isNull(selfTrigger) | Objects.isNull(selfTarget)){
                res.append("内字段 " + greaterItem.getKey() + "和外字段 " + greaterItem.getValue() + "存在null.");
            } else if(selfTrigger <= selfTarget){
                res.append("内字段 " + greaterItem.getKey() + "和外字段 " + greaterItem.getValue() + "的大小关系不满足.");
            }

        }
        for (Map.Entry<String, String> greaterItem : this.outterGreaterMap.entrySet()){
            Integer ouuerTrigger = getFieldValue(binlogTargetData, greaterItem.getKey(), Integer.class);
            Integer OtterTarget = getFieldValue(binlogTargetData, greaterItem.getValue(), Integer.class);
            if (Objects.isNull(ouuerTrigger) | Objects.isNull(OtterTarget)){
                res.append("内字段 " + greaterItem.getKey() + "和外字段 " + greaterItem.getValue() + "存在null.");
            } else if(ouuerTrigger <= OtterTarget){
                res.append("内字段 " + greaterItem.getKey() + "和外字段 " + greaterItem.getValue() + "的大小关系不满足.");
            }
        }
        return res.toString();
    }

    public String checkLess(BinlogRawData binlogTriggerData, BinlogRawData binlogTargetData){
        StringBuilder res = new StringBuilder();
        for (Map.Entry<String, String> greaterItem : this.lessMap.entrySet()){
            Integer trigger = getFieldValue(binlogTriggerData, greaterItem.getKey(), Integer.class);
            Integer target = getFieldValue(binlogTargetData, greaterItem.getValue(), Integer.class);
            if (Objects.isNull(trigger) | Objects.isNull(target)){
                res.append("内字段 " + greaterItem.getKey() + "和外字段 " + greaterItem.getValue() + "存在null.");
            } else if(trigger >= target){
                res.append("内字段 " + greaterItem.getKey() + "和外字段 " + greaterItem.getValue() + "的大小关系不满足.");
            }

        }
        for (Map.Entry<String, String> greaterItem : this.selfLessMap.entrySet()){
            Integer selfTrigger = getFieldValue(binlogTriggerData, greaterItem.getKey(), Integer.class);
            Integer selfTarget = getFieldValue(binlogTriggerData, greaterItem.getValue(), Integer.class);
            if (Objects.isNull(selfTrigger) | Objects.isNull(selfTarget)){
                res.append("内字段 " + greaterItem.getKey() + "和外字段 " + greaterItem.getValue() + "存在null.");
            } else if(selfTrigger >= selfTarget){
                res.append("内字段 " + greaterItem.getKey() + "和外字段 " + greaterItem.getValue() + "的大小关系不满足.");
            }

        }
        for (Map.Entry<String, String> greaterItem : this.outterLessMap.entrySet()){
            Integer trigger = getFieldValue(binlogTargetData, greaterItem.getKey(), Integer.class);
            Integer target = getFieldValue(binlogTargetData, greaterItem.getValue(), Integer.class);
            if (Objects.isNull(trigger) | Objects.isNull(target)){
                res.append("内字段 " + greaterItem.getKey() + "和外字段 " + greaterItem.getValue() + "存在null.");
            } else if(trigger >= target){
                res.append("内字段 " + greaterItem.getKey() + "和外字段 " + greaterItem.getValue() + "的大小关系不满足.");
            }
        }
        return res.toString();
    }

    public String checkEqual(BinlogRawData binlogTriggerData, BinlogRawData binlogTargetData){
        StringBuilder res = new StringBuilder();
        for (Map.Entry<String, String> greaterItem : this.equalMap.entrySet()){
            Integer trigger = getFieldValue(binlogTriggerData, greaterItem.getKey(), Integer.class);
            Integer target = getFieldValue(binlogTargetData, greaterItem.getValue(), Integer.class);
            if (Objects.isNull(trigger) | Objects.isNull(target)){
                res.append("内字段 " + greaterItem.getKey() + "和外字段 " + greaterItem.getValue() + "存在null.");
            } else if(!trigger.equals(target)){
                res.append("内字段 " + greaterItem.getKey() + "和外字段 " + greaterItem.getValue() + "的相等关系不满足.");
            }

        }
        return res.toString();
    }

    public String checkEmun(BinlogRawData binlogTriggerData, BinlogRawData binlogTargetData){
        StringBuilder res = new StringBuilder();
        for (Map.Entry<String, String> greaterItem : this.greaterMap.entrySet()){
            Integer trigger = getFieldValue(binlogTriggerData, greaterItem.getKey(), Integer.class);
            Integer target = getFieldValue(binlogTargetData, greaterItem.getValue(), Integer.class);
            if (Objects.isNull(trigger) | Objects.isNull(target)){
                res.append("内字段 " + greaterItem.getKey() + "和外字段 " + greaterItem.getValue() + "存在null.");
            } else if(trigger > target){
                res.append("内字段 " + greaterItem.getKey() + "和外字段 " + greaterItem.getValue() + "的大小关系不满足.");
            }

        }
        for (Map.Entry<String, String> greaterItem : this.selfGreaterMap.entrySet()){
            Integer selfTrigger = getFieldValue(binlogTriggerData, greaterItem.getKey(), Integer.class);
            Integer selfTarget = getFieldValue(binlogTriggerData, greaterItem.getValue(), Integer.class);
            if (Objects.isNull(selfTrigger) | Objects.isNull(selfTarget)){
                res.append("内字段 " + greaterItem.getKey() + "和外字段 " + greaterItem.getValue() + "存在null.");
            } else if(selfTrigger > selfTarget){
                res.append("内字段 " + greaterItem.getKey() + "和外字段 " + greaterItem.getValue() + "的大小关系不满足.");
            }

        }
        for (Map.Entry<String, String> greaterItem : this.outterGreaterMap.entrySet()){
            Integer trigger = getFieldValue(binlogTargetData, greaterItem.getKey(), Integer.class);
            Integer target = getFieldValue(binlogTargetData, greaterItem.getValue(), Integer.class);
            if (Objects.isNull(trigger) | Objects.isNull(target)){
                res.append("内字段 " + greaterItem.getKey() + "和外字段 " + greaterItem.getValue() + "存在null.");
            } else if(trigger > target){
                res.append("内字段 " + greaterItem.getKey() + "和外字段 " + greaterItem.getValue() + "的大小关系不满足.");
            }
        }
        return res.toString();
    }


    @Override
    public void alarm(String checkResult, RawData triggerData, RawData... targetData) throws Exception {
        super.alarm(checkResult, triggerData, targetData);
    }
}
