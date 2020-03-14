package com.yi2580.easypay.wechat;

/**
 * @author zhangqi
 * Date:2019/3/8
 * Description:微信支付结果回调
 */
public interface WechatPayResultCallBack {
    /**
     * 支付成功
     */
    void onSuccess(String prepayId, String extData);

    /**
     * 支付失败
     */
    void onError(int errorCode, String message);

    /**
     * 支付取消
     */
    void onCancel();
}
