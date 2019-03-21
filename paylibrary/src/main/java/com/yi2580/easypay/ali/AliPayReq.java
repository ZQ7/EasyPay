package com.yi2580.easypay.ali;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.yi2580.easypay.EasyPay;
import com.yi2580.easypay.bean.AliBizContent;
import com.yi2580.easypay.bean.AliPayResult;
import com.yi2580.easypay.utils.ALiPayUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 支付宝支付请求
 * 支付宝APP支付文档 https://docs.open.alipay.com/204
 */
public class AliPayReq {

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    private AliPayResultCallBack mCallback;

    private Activity mActivity;

    //订单信息
    private String orderInfo;

    private Handler mHandler;

    public AliPayReq() {
        super();
        mHandler = new MyHandler();
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    if (mCallback != null) {
                        AliPayResult payResult = new AliPayResult((Map<String, String>) msg.obj);
                        /**
                         * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                         */
                        String resultInfo = payResult.getResult();
                        String resultStatus = payResult.getResultStatus();
                        String memo = payResult.getMemo();
                        // 判断resultStatus 为9000则代表支付成功
                        if (TextUtils.equals(resultStatus, AliPayAPI.ALI_PAY_SUCCESS)) {
                            // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                            mCallback.onSuccess();
                        } else if (TextUtils.equals(resultStatus, AliPayAPI.ALI_PAY_DEALING)) {
                            //正在处理中，支付结果未知
                            mCallback.onDealing();
                        } else if (TextUtils.equals(resultStatus, AliPayAPI.ALI_PAY_CANCEL)) {
                            //用户取消zhifu
                            mCallback.onCancel();
                        } else {
                            // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                            mCallback.onError(resultStatus, memo);
                        }
                    }
                }
                break;
                default:
            }
        }
    }


    /**
     * 发送支付宝支付请求
     */
    public void send() {
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(mActivity);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * 设置回调事件
     *
     * @param callBack 回调
     * @return
     */
    public AliPayReq setResultCallBack(AliPayResultCallBack callBack) {
        this.mCallback = callBack;
        return this;
    }

    public static class Builder {
        //上下文
        private Activity activity;

        //支付宝分配给开发者的应用ID
        private String appId = EasyPay.ALI_PAY_APP_ID;
        //支付宝服务器主动通知商户服务器里指定的页面http/https路径。建议商户使用https
        private String notifyUrl = EasyPay.ALI_PAY_NOTIFY_URL;
        //发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
        private String timestamp;

        //接口名称
        private String method = "alipay.trade.app.pay";

        //商户请求参数的签名串
        private String sign;
        //pkcs8 格式的商户私钥。
        private String privateKey;
        //商户生成签名字符串所使用的签名算法类型，目前支持RSA2和RSA，推荐使用RSA2
        private String signType = "RSA2";

        //仅支持JSON
        private String format = "JSON";
        //调用的接口版本，固定为：1.0
        private String version = "1.0";
        //请求使用的编码格式，如utf-8,gbk,gb2312等
        //private String charset = ALiPayUtils.DEFAULT_CHARSET;
        private String charset = "utf-8";


        private AliBizContent bizContent;

        public Builder() {
            super();
        }

        public Builder with(Activity activity) {
            this.activity = activity;
            return this;
        }

        /**
         * 设置支付宝支付AppID
         *
         * @param appId AppId
         * @return
         */
        public Builder setAppId(String appId) {
            this.appId = appId;
            return this;
        }

        /**
         * 商户请求参数的签名串
         *
         * @param sign https://docs.open.alipay.com/291/105974
         * @return
         */
        public Builder setSign(String sign) {
            this.sign = sign;
            return this;
        }

        /**
         * RSA2或者RSA
         *
         * @param signType
         * @return
         */
        public Builder setSignType(String signType) {
            this.signType = signType;
            return this;
        }

        /**
         * 发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"
         *
         * @param timestamp
         * @return
         */
        public Builder setTimestamp(String timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        /**
         * 业务请求参数的集合
         *
         * @param bizContent
         * @return
         */
        public Builder setBizContent(AliBizContent bizContent) {
            this.bizContent = bizContent;
            return this;
        }

        /**
         * 支付宝服务器主动通知商户服务器里指定的页面
         *
         * @param notifyUrl
         * @return
         */
        public Builder setNotifyUrl(String notifyUrl) {
            this.notifyUrl = notifyUrl;
            return this;
        }

        /**
         * 设置方法名
         *
         * @param method
         * @return
         */
        public Builder setMethod(String method) {
            this.method = method;
            return this;
        }

        /**
         * 使用私钥客户端完成签名
         *
         * @param privateKey
         * @return
         */
        public Builder setPrivateKey(String privateKey) {
            this.privateKey = privateKey;
            return this;
        }

        public AliPayReq create() {
            //如果签名和私钥同时为空，抛出异常
            if (TextUtils.isEmpty(sign) && TextUtils.isEmpty(privateKey))
                throw new NullPointerException("sign is null");

            if (bizContent == null)
                throw new NullPointerException("AliBizContent is null");

            if (TextUtils.isEmpty(timestamp)) {
                timestamp = ALiPayUtils.getTimestamp();
            }

            //判断签名方式
            boolean rsa2 = TextUtils.equals(signType, "RSA2");

            Map<String, String> keyValues = new HashMap<String, String>();
            keyValues.put("app_id", appId);
            keyValues.put("method", method);
            keyValues.put("format", format);
            keyValues.put("charset", charset);
            keyValues.put("sign_type", signType);
            keyValues.put("timestamp", timestamp);
            keyValues.put("version", version);
            keyValues.put("notify_url", notifyUrl);
            keyValues.put("biz_content", bizContent.toJson());

            String orderParam = ALiPayUtils.buildOrderParam(keyValues);

            //签名为空，私钥不为空，本地生成签名
            if (TextUtils.isEmpty(sign) && !TextUtils.isEmpty(privateKey)) {
                sign = ALiPayUtils.getSign(keyValues, privateKey, rsa2);
            }


            AliPayReq aliPayReq = new AliPayReq();
            aliPayReq.mActivity = this.activity;
            aliPayReq.orderInfo = orderParam + "&sign=" + sign;

            /*Map<String, String> params = ALiPay.buildOrderParamMap(bizContent.getOut_trade_no(), "测试", "0.01");
            String orderParam = ALiPay.buildOrderParam(params);
            String sign = ALiPay.getSign(params, privateKey);

            AliPayReq aliPayReq = new AliPayReq();
            aliPayReq.mActivity = this.activity;
            aliPayReq.orderInfo = orderParam + "&" + sign;*/

            return aliPayReq;
        }

    }

}
