package com.yi2580.easypay;

import com.yi2580.easypay.ali.AliPayAPI;
import com.yi2580.easypay.ali.AliPayReq;
import com.yi2580.easypay.wechat.WechatPayAPI;
import com.yi2580.easypay.wechat.WechatPayReq;

/**
 * @author zhangqi
 * Date:2019/3/8
 * Description:
 */
public class EasyPay {

    /**
     * 微信AppId和商户Id
     */
    public static String WECHAT_PAY_APP_ID = "";
    public static String WECHAT_PAY_PARTNER_ID = "";

    /**
     * 支付宝AppId和支付回调服务器地址
     */
    public static String ALI_PAY_APP_ID = "";
    public static String ALI_PAY_NOTIFY_URL = "";

    private static final Object mLock = new Object();
    private static EasyPay mInstance;

    public static EasyPay getInstance() {
        if (mInstance == null) {
            synchronized (mLock) {
                if (mInstance == null) {
                    mInstance = new EasyPay();
                }
            }
        }
        return mInstance;
    }

    /**
     * 微信支付请求
     *
     * @param wechatPayReq
     */
    public void sendPayRequest(WechatPayReq wechatPayReq) {
        WechatPayAPI.getInstance().sendPayReq(wechatPayReq);
    }

    /**
     * 支付宝支付请求
     *
     * @param aliPayReq
     */
    public void sendPayRequest(AliPayReq aliPayReq) {
        AliPayAPI.getInstance().sendPayReq(aliPayReq);
    }

}
