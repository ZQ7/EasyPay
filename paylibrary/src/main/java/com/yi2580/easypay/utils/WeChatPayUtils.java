package com.yi2580.easypay.utils;

import android.text.TextUtils;

import com.tencent.mm.opensdk.modelpay.PayReq;

import java.security.MessageDigest;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author zhangqi
 * Date:2019/3/16
 * Description:微信支付工具类
 */
public class WeChatPayUtils {

    /**
     * @param appId        微信支付AppID
     * @param partnerId    微信支付商户号
     * @param prepayId     预支付码
     * @param packageValue 包名
     * @param nonceStr     随机字符串
     * @param timeStamp    时间戳
     * @param sign         签名
     * @param privateKey   私钥
     * @return
     */
    public static PayReq getPayReq(String appId, String partnerId, String prepayId,
                                   String packageValue, String nonceStr, String timeStamp, String sign, String privateKey) {
        if (TextUtils.isEmpty(nonceStr)) {
            nonceStr = genNonceStr();
        }
        if (TextUtils.isEmpty(timeStamp)) {
            timeStamp = getTimeStamp();
        }

        if (TextUtils.isEmpty(sign)) {
            TreeMap<String, String> treeMap = new TreeMap<>();
            treeMap.put("appid", appId);
            treeMap.put("noncestr", nonceStr);
            treeMap.put("package", packageValue);
            treeMap.put("partnerid", partnerId);
            treeMap.put("prepayid", prepayId);
            treeMap.put("timestamp", timeStamp);
            sign = genSign(treeMap, privateKey);
        }

        PayReq payReq = new PayReq();
        payReq.appId = appId;
        payReq.partnerId = partnerId;
        payReq.prepayId = prepayId;
        payReq.nonceStr = nonceStr;
        payReq.timeStamp = timeStamp;
        payReq.packageValue = packageValue;
        payReq.sign = sign;

        return payReq;
    }

    /**
     * 获取时间戳
     *
     * @return
     */
    private static String getTimeStamp() {
        //System.currentTimeMillis() 获取毫秒
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

    /**
     * 随机字符串，不长于32位
     *
     * @return
     */
    private static String genNonceStr() {
        Random random = new Random();
        return getMessageDigest(String.valueOf(random.nextInt(Integer.MAX_VALUE)).getBytes());
    }

    /**
     * 获取签名
     *
     * @param treeMap
     * @return
     */
    private static String genSign(TreeMap<String, String> treeMap, String privateKey) {
        StringBuilder sb = new StringBuilder();
        Set<Map.Entry<String, String>> set = treeMap.entrySet();
        Iterator<Map.Entry<String, String>> iterator = set.iterator();
        String key;
        while (iterator.hasNext()) {
            key = iterator.next().getKey();
            sb.append(key);
            sb.append('=');
            sb.append(treeMap.get(key));
            sb.append('&');
        }
        sb.append("key=");
        sb.append(privateKey);
        String sign = getMessageDigest(sb.toString().getBytes()).toUpperCase();
        return sign;
    }

    private static String getMessageDigest(byte[] buffer) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            // 获取MD5转换器
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(buffer);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            //转成32位的字符串
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
}
