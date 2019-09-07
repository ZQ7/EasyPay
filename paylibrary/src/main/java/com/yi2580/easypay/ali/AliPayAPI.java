package com.yi2580.easypay.ali;

/**
 * 支付宝支付API
 */
public class AliPayAPI {

    public static final String ALI_PAY_SUCCESS = "9000";
    public static final String ALI_PAY_DEALING = "8000";
    public static final String ALI_PAY_CANCEL = "6001";

    private AliPayReq mPayReq;
    /**
     * 获取支付宝支付API
     */
    private static final Object mLock = new Object();
    private static AliPayAPI mInstance;

    public static AliPayAPI getInstance() {
        if (mInstance == null) {
            synchronized (mLock) {
                if (mInstance == null) {
                    mInstance = new AliPayAPI();
                }
            }
        }
        return mInstance;
    }


    /**
     * 发送支付宝支付请求
     *
     * @param aliPayReq
     */
    public void sendPayReq(AliPayReq aliPayReq) {
        if (this.mPayReq!=null){
            this.mPayReq.release();
            this.mPayReq = null;
        }
        this.mPayReq = aliPayReq;
        aliPayReq.send();
    }

    public void release() {
        if (this.mPayReq != null) {
            this.mPayReq.release();
            this.mPayReq = null;
            mInstance = null;
        }
    }


}
