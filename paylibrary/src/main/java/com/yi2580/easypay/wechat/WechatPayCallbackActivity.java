package com.yi2580.easypay.wechat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.yi2580.easypay.R;

/**
 * @author zhangqi
 * Date:2019/3/8
 * Description:微信支付回调Activity
 */
public class WechatPayCallbackActivity extends Activity implements IWXAPIEventHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat_pay_callback);

        if (WechatPayAPI.getInstance().getPayReq() != null
                && WechatPayAPI.getInstance().getPayReq().getWXApi() != null) {
            WechatPayAPI.getInstance().getPayReq().getWXApi().handleIntent(getIntent(), this);
        } else {
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        if (WechatPayAPI.getInstance().getPayReq() != null
                && WechatPayAPI.getInstance().getPayReq().getWXApi() != null) {
            WechatPayAPI.getInstance().getPayReq().getWXApi().handleIntent(intent, this);
        }
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if(baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if(WechatPayAPI.getInstance().getPayReq() != null) {
                WechatPayAPI.getInstance().getPayReq().onResp(baseResp);
                finish();
            }
        }
    }
}
