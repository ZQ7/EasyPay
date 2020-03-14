package com.yi2580.easypay.ali;

/**
 * @author zhangqi
 * Date:2019/3/8
 * Description:支付宝支付结果回调
 */
public interface AliPayResultCallBack {
    /**
     * 支付成功
     */
    void onSuccess(String result);

    /**
     * 正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
     */
    void onDealing(String result);

    /**
     * 支付失败
     * https://docs.open.alipay.com/204/105301/
     *
     * @param resultStatus
     */
    void onError(String resultStatus, String message);

    /**
     * 支付取消
     */
    void onCancel();
}
