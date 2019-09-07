package com.yi2580.easypay.wechat;

/**
 * 微信支付API
 */
public class WechatPayAPI {

    public static final int WECHAT_PAY_SUCCESS = 0;
    public static final int WECHAT_PAY_FAIL = -1;
    public static final int WECHAT_PAY_CANCEL = -2;

    private WechatPayReq mPayReq;

    /**
     * 获取微信支付API
     */
    private static final Object mLock = new Object();
    private static WechatPayAPI mInstance;

    public static WechatPayAPI getInstance() {
        if (mInstance == null) {
            synchronized (mLock) {
                if (mInstance == null) {
                    mInstance = new WechatPayAPI();
                }
            }
        }
        return mInstance;
    }

    /**
     * 发送微信支付请求
     *
     * @param wechatPayReq
     */
    public void sendPayReq(WechatPayReq wechatPayReq) {
        if (this.mPayReq!=null){
            this.mPayReq.release();
            this.mPayReq = null;
        }
        this.mPayReq = wechatPayReq;
        wechatPayReq.send();
    }

    /**
     * 获取PayReq
     * @return
     */
    public WechatPayReq getPayReq() {
        return mPayReq;
    }

    public void release() {
        if (this.mPayReq != null) {
            this.mPayReq.release();
            this.mPayReq = null;
            mInstance = null;
        }
    }
}
