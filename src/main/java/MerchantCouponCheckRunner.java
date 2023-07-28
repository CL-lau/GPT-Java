import com.dianping.frog.sdk.alarm.DXUtil;
import com.dianping.frog.sdk.data.BinlogRawData;
import com.dianping.frog.sdk.data.ColumnInfo;
import com.dianping.frog.sdk.data.RawData;
import com.dianping.frog.sdk.runner.DefaultRuleRunner;
import com.dianping.frog.sdk.util.BcpLoggerUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

public class MerchantCouponCheckRunner extends DefaultRuleRunner {
    /**
     * bcp券类型
     */
    private static final Set<Integer> checkCouponTypeSet = new HashSet<>();
    /**
     * 触发流 要校验的字段：
     */
    private static final Map<String, Class> toCheckFieldTriggerMap = new HashMap<>();
    /**
     * 目标流字段（上游或下游的表）
     */
    private static final Map<String, Class> toCheckFieldTargetMap = new HashMap<>();
    /**
     * 特殊校验字段（一般是json字段） 要校验的字段：
     */
    private static final Set<String> specialFieldSet = new HashSet<>();

    /**
     * json字段 要转换的json字段：
     */
    private static final Set<String> jsonFieldSet = new HashSet<>();

    {
        //商家券
        checkCouponTypeSet.add(13000);

        toCheckFieldTriggerMap.put("activity_type", Integer.class);
        toCheckFieldTargetMap.put("activity_type", Integer.class);

        // 金额（分）
        toCheckFieldTriggerMap.put("detail", String.class);
        toCheckFieldTargetMap.put("detail", String.class);

        // 门槛（分）
        toCheckFieldTriggerMap.put("limit_price", Integer.class);
        toCheckFieldTargetMap.put("limit_price", Integer.class);

        // 使用时段设置，用券开始时间
        toCheckFieldTriggerMap.put("valid_start_time", Integer.class);
        // 使用时段设置，用券结束时间
        toCheckFieldTriggerMap.put("valid_end_time", Integer.class);
        // 使用时段设置，有效期，多少秒后过期；和业务透传biz_pass_extend 会配合使用
        toCheckFieldTriggerMap.put("validity", Integer.class);
        // 券使用类型 1 自定义时间段 2 领取后N天有效 3 领取后当天有效 4 领取后N小时有效
        toCheckFieldTriggerMap.put("use_type", Integer.class);
        specialFieldSet.add("use_type");
        specialFieldSet.add("valid_start_time");
        specialFieldSet.add("valid_end_time");
        specialFieldSet.add("validity");
        toCheckFieldTargetMap.put("valid_start_time", Integer.class);
        toCheckFieldTargetMap.put("valid_end_time", Integer.class);
        toCheckFieldTargetMap.put("validity", Integer.class);

        // 每个用户活动期间发券限制数量即单用户限领总张数
        toCheckFieldTriggerMap.put("limit_count", Integer.class);
        toCheckFieldTargetMap.put("limit_count", Integer.class);
        // 券总数即券维度发放总张数
        toCheckFieldTriggerMap.put("total_count", Integer.class);
        toCheckFieldTargetMap.put("total_count", Integer.class);


        // 发券限制条件，发放人群设置:团好货新客、团好货老客、团好货自定义人群、不限，不需要base64 decode
        toCheckFieldTriggerMap.put("opt_send_coupon_limit", String.class);
        specialFieldSet.add("opt_send_coupon_limit");
        toCheckFieldTargetMap.put("extend", String.class);

        //json字段 需要base64 decode
        jsonFieldSet.add("extend");

    }

    @Override
    public String check(RawData triggerData, RawData... targetData) throws Exception {
        BinlogRawData binlogTriggerData = (BinlogRawData) triggerData;
        Integer thhCouponId = getFieldValue(binlogTriggerData, "origin_coupon_id", Integer.class);
        if (targetData == null || targetData.length == 0) {
            return "thh商家券活动变更，origin_coupon_id:"+thhCouponId+"，tsp无变更";
        }

        BinlogRawData binlogTargetData = (BinlogRawData) targetData[0];
        Integer tspCouponId = getFieldValue(binlogTargetData, "id", Integer.class);

        if (Objects.equals(tspCouponId, thhCouponId)) {
            Integer couponType = getFieldValue(binlogTriggerData, "activity_type", Integer.class);
            if (!checkCouponTypeSet.contains(couponType)) {
                return null;
            }
            String diff;
            try {
                diff = checkDiff(tspCouponId, couponType, binlogTriggerData, binlogTargetData);
            } catch (Exception e) {
                diff = "商家券信息比对异常:" + e.getMessage() + "_" + e.getMessage() + "_" + e;
                e.printStackTrace();
            }
            if (StringUtils.isNotBlank(diff)) {
                diff += ",tsp券配置和团好货营销数据不一致，可能引发资损";
            }
            return diff;
        }
        return null;
    }

    private <T> T getFieldValue(BinlogRawData binlogRawData, String fieldName, Class<T> returnType) {
        ColumnInfo columnInfo = binlogRawData.getColumnInfoMap().get(fieldName);
        Object object = columnInfo.getNewValue();
        if (object != null && returnType != null) {
            return (T) object;
        }
        return null;
    }

    private String checkDiff(Integer tspCouponId, Integer couponType, BinlogRawData binlogTriggerData,
                             BinlogRawData binlogTargetData) {
        StringBuilder diff = new StringBuilder();

        Map<String, Object> triggerMap = new HashMap<>(8);
        for (Map.Entry<String, ColumnInfo> triggerEntry : binlogTriggerData.getColumnInfoMap().entrySet()) {
            //对于每个字段处理业务逻辑
            String key = triggerEntry.getKey();
            if (toCheckFieldTriggerMap.containsKey(key)) {
                ColumnInfo columnInfo = binlogTriggerData.getColumnInfoMap().get(key);
                Object val = columnInfo.getNewValue();
                if (jsonFieldSet.contains(key)) {
//                    triggerMap.put(key, fakeByteArr2JsonStr(val.toString()));
                    // todo 调试的时候需要打开上面代码，注释掉下面代码
                    String value = new String(Base64.getDecoder().decode(val.toString().replaceAll("\r\n", "")));
                    triggerMap.put(key, value);
                } else {
                    triggerMap.put(key, getFieldValue(binlogTriggerData, key, toCheckFieldTriggerMap.get(key)));
                }
            }
        }
        Map<String, Object> targetMap = new HashMap<>(8);
        for (Map.Entry<String, ColumnInfo> targetEntry : binlogTargetData.getColumnInfoMap().entrySet()) {
            //对于每个字段处理业务逻辑
            String key = targetEntry.getKey();
            if (toCheckFieldTargetMap.containsKey(key)) {
                ColumnInfo columnInfo = binlogTargetData.getColumnInfoMap().get(key);
                Object val = columnInfo.getNewValue();
                if (jsonFieldSet.contains(key)) {
//                    targetMap.put(key, fakeByteArr2JsonStr(val.toString()));
                    // todo 调试的时候需要打开上面代码，注释掉下面代码
                    String value = new String(Base64.getDecoder().decode(val.toString().replaceAll("\r\n", "")));
                    targetMap.put(key, value);
                } else {
                    targetMap.put(key, getFieldValue(binlogTargetData, key, toCheckFieldTargetMap.get(key)));
                }
            }
        }

        for (Map.Entry<String, Object> triggerMapEntry : triggerMap.entrySet()) {
            if (!specialFieldSet.contains(triggerMapEntry.getKey())){
                if(!Objects.equals(triggerMapEntry.getValue(), targetMap.get(triggerMapEntry.getKey()))) {
                    diff.append("券id:" + tspCouponId + ",类型:" + couponType + ",不一致字段:" + triggerMapEntry.getKey() + ",tsp券配置值:" + triggerMapEntry.getValue() + ",团好货值:" + targetMap.get(triggerMapEntry.getKey()));
                }
            } else{
                if ("use_type".equals(triggerMapEntry.getKey())) {
                    diff.append(specialFieldCheckDiff4UseType(triggerMapEntry.getValue().toString(), triggerMap, targetMap));
                } else if ("opt_send_coupon_limit".equals(triggerMapEntry.getKey())) {
                    diff.append(specialFieldCheckDiff4optSendCouponLimit(triggerMapEntry.getValue().toString(), targetMap));
                }
            }
        }

        return diff.toString();
    }

    private String specialFieldCheckDiff4UseType(String thhUseType, Map<String, Object> triggerMap,
                                                 Map<String, Object> targetMap) {
        StringBuilder diff = new StringBuilder();
        // 券使用时间类型
        // thh:1-自定义时间段，2-领取后n天，
        String thhValidity = triggerMap.get("validity") == null ? "" : triggerMap.get("validity").toString();
        String thhValidStartTime = triggerMap.get("valid_start_time") == null ? "" : triggerMap.get("valid_start_time").toString();
        String thhValidEndTime = triggerMap.get("valid_end_time") == null ? "" : triggerMap.get("valid_end_time").toString();

        String tspValidity = targetMap.get("validity") == null ? "" : targetMap.get("validity").toString();
        String tspValidStartTime = targetMap.get("valid_start_time") == null ? "" : targetMap.get("valid_start_time").toString();
        String tspValidEndTime = targetMap.get("valid_end_time") == null ? "" : targetMap.get("valid_end_time").toString();
        if ("1".equals(thhUseType)) {
            // 自定义使用时间
            if (!tspValidity.equals(thhValidity) || !tspValidStartTime.equals(thhValidStartTime) || !tspValidEndTime.equals(thhValidEndTime)) {
                diff.append(buildDiffContentByField("自定义使用时间", tspValidStartTime + "_" + tspValidEndTime + "_" + tspValidity, thhValidStartTime + "_" + thhValidEndTime + "_" + thhValidity));
            }
        } else if ("2".equals(thhUseType)) {
            //领取后n天有效，配合validDays
            if (!!tspValidity.equals(thhValidity)) {
                diff.append(buildDiffContentByField("领取后n天有效", tspValidity, thhValidity));
            }
        }

        return diff.toString();
    }

    /**
     * 1商详页可领 0不可领
     */
    private String specialFieldCheckDiff4optSendCouponLimit(String thhSendCouponLimit, Map<String, Object> targetMap) {
        StringBuilder diff = new StringBuilder();
        JsonElement tspJsonElement = targetMap.get("extend") == null ? null : new JsonParser().parse(targetMap.get("extend").toString());
        JsonObject tspJsonObject = tspJsonElement == null ? null : tspJsonElement.getAsJsonObject();

        JsonElement thhJsonElement = new JsonParser().parse(thhSendCouponLimit);
        JsonObject thhJsonObject = thhJsonElement == null ? null : thhJsonElement.getAsJsonObject();

        // tsp券领取位置：1-商详页
        String tspGetArea = tspJsonObject == null || tspJsonObject.get("ex_sa_thh_page_type") == null ? "" : tspJsonObject.get("ex_sa_thh_page_type").getAsString();
        String thhGetArea = thhJsonObject == null || thhJsonObject.get("getArea") == null ? "" : thhJsonObject.get("getArea").getAsString();
        if (!tspGetArea.equals(thhGetArea)) {
            diff.append(buildDiffContentByField("getArea", tspGetArea, thhGetArea));
        }
        return diff.toString();
    }

    private static String buildDiffContentByField(String fieldKey, Object tspFieldVal, Object thhFieldVal) {
        if (!tspFieldVal.equals(thhFieldVal)) {
            return String.format("tsp券%s:%s,thh券%s:%s\r\n", fieldKey, tspFieldVal, fieldKey, thhFieldVal);
        } else {
            return "";
        }
    }

    @Override
    public void alarm(String checkResult, RawData triggerData, RawData... targetData) {
        if (StringUtils.isNotBlank(checkResult)) {
            DXUtil.sendAlarm(checkResult);
        }
    }

    //todo 调试的时候才用
    private static String fakeByteArr2JsonStr(String fakeByteArrStr) {
        byte[] thhBytes = new Gson().fromJson(fakeByteArrStr, new TypeToken<byte[]>() {
        }.getType());
        return new String(thhBytes);
    }
}