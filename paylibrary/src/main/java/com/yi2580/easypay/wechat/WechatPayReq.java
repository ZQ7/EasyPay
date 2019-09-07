package com.yi2580.easypay.wechat;

import android.app.Activity;
import android.text.TextUtils;

import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.yi2580.easypay.EasyPay;
import com.yi2580.easypay.utils.WeChatPayUtils;


/**
 * 微信支付请求
 * 微信APP支付文档 https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=8_1
 *
 * @author Administrator
 */
public class WechatPayReq {

    private static final String TAG = WechatPayReq.class.getSimpleName();

    private WechatPayResultCallBack mCallback;

    private Activity mActivity;

    private PayReq mPayReq;

    private IWXAPI mWXApi;

    public WechatPayReq() {
        super();
    }


    /**
     * 发送微信支付请求
     */
    public void send() {
        if (mPayReq == null) {
            return;
        }
        mWXApi.sendReq(mPayReq);
    }

    /**
     * 支付回调响应
     *
     * @param baseResp
     */
    public void onResp(BaseResp baseResp) {
        if (mCallback == null) {
            return;
        }
        if (baseResp.errCode == WechatPayAPI.WECHAT_PAY_SUCCESS) {
            //成功
            mCallback.onSuccess();
        } else if (baseResp.errCode == WechatPayAPI.WECHAT_PAY_CANCEL) {
            //取消
            mCallback.onCancel();
        } else {
            //错误
            mCallback.onError(baseResp.errCode, baseResp.errStr);
        }
        mCallback = null;
    }


    public WechatPayReq setWechatPayResultCallBack(WechatPayResultCallBack wechatPayListener) {
        this.mCallback = wechatPayListener;
        return this;
    }

    public IWXAPI getWXApi() {
        return mWXApi;
    }

    public static class Builder {
        //上下文
        private Activity activity;
        //微信支付AppID
        private String appId = EasyPay.WECHAT_PAY_APP_ID;
        //微信支付商户号
        private String partnerId = EasyPay.WECHAT_PAY_PARTNER_ID;
        //预支付码（重要）
        private String prepayId;
        //"Sign=WXPay"
        private String packageValue = "Sign=WXPay";
        private String nonceStr;
        //时间戳
        private String timeStamp;
        //签名
        private String sign;
        //API密钥，在商户平台设置
        private String privateKey;

        public Builder() {
            super();
        }

        public Builder with(Activity activity) {
            this.activity = activity;
            return this;
        }

        /**
         * 设置微信支付AppID
         *
         * @param appId
         * @return
         */
        public Builder setAppId(String appId) {
            this.appId = appId;
            return this;
        }

        /**
         * 微信支付商户号
         *
         * @param partnerId
         * @return
         */
        public Builder setPartnerId(String partnerId) {
            this.partnerId = partnerId;
            return this;
        }

        /**
         * 设置预支付码（重要）
         *
         * @param prepayId
         * @return
         */
        public Builder setPrepayId(String prepayId) {
            this.prepayId = prepayId;
            return this;
        }


        /**
         * 设置
         *
         * @param packageValue
         * @return
         */
        public Builder setPackageValue(String packageValue) {
            this.packageValue = packageValue;
            return this;
        }


        /**
         * 设置
         *
         * @param nonceStr
         * @return
         */
        public Builder setNonceStr(String nonceStr) {
            this.nonceStr = nonceStr;
            return this;
        }

        /**
         * 设置时间戳
         *
         * @param timeStamp
         * @return
         */
        public Builder setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
            return this;
        }

        /**
         * 设置签名
         *
         * @param sign
         * @return
         */
        public Builder setSign(String sign) {
            this.sign = sign;
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

        public WechatPayReq create() {
            //如果签名和私钥同时为空，抛出异常
            if (TextUtils.isEmpty(sign) && TextUtils.isEmpty(privateKey))
                throw new NullPointerException("sign is null");

            PayReq payReq = WeChatPayUtils.getPayReq(appId, partnerId, prepayId, packageValue, nonceStr, timeStamp, sign, privateKey);

            WechatPayReq wechatPayReq = new WechatPayReq();

            wechatPayReq.mActivity = this.activity;
            wechatPayReq.mPayReq = payReq;

            //微信核心API
            wechatPayReq.mWXApi = WXAPIFactory.createWXAPI(this.activity, null);
            wechatPayReq.mWXApi.registerApp(this.appId);
            return wechatPayReq;
        }

    }

    protected void release() {
        this.mWXApi = null;
        this.mPayReq = null;
        this.mCallback = null;
        this.mActivity = null;
    }

}
