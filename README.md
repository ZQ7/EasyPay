# EasyPay

[ ![Download](https://api.bintray.com/packages/zhangqi/maven/EasyPay/images/download.svg?version=1.0.0) ](https://bintray.com/zhangqi/maven/EasyPay/1.0.0/link)

> 对微信支付和支付宝支付的App端SDK进行二次封装，对外提供一个较为简单的接口和支付结果回调


## 相关文档
1. 支付宝APP支付文档：
https://docs.open.alipay.com/204

2. 微信APP支付文档：
https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=8_1

## 1. 如何添加

#### 在app目录下的build.gradle中添加依赖

```gradle
implementation 'com.yi2580.easypay:EasyPay:1.0.0'
```

## 2. Android Manifest配置

**注册activity**

```xml
        <!-- 微信支付 -->
        <activity-alias
            android:name="微信支付注册时的包名.wxapi.WXPayEntryActivity"
            android:exported="true"
            android:launchMode="singleTop"
            android:targetActivity="com.yi2580.easypay.wechat.WechatPayCallbackActivity" />
```

**全局初始化AppId及相关(也可以在发起支付的时候设置)**

```java
//支付宝AppId和通知回调地址
EasyPay.ALI_PAY_APP_ID = ConstantValue.ALI_APP_ID;
EasyPay.ALI_PAY_NOTIFY_URL = ConstantValue.ALI_NOTIFY_URL;
//微信AppId和商户号
EasyPay.WECHAT_PAY_APP_ID = ConstantValue.WX_APP_ID;
EasyPay.WECHAT_PAY_PARTNER_ID = ConstantValue.WX_MCH_ID;
```


## 3. 发起支付

### 3.1 微信支付

```java
        //微信请求参数文档https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_12&index=2
        WechatPayReq req = new WechatPayReq.Builder()
                .with(mActivity)
                .setPrepayId("预支付交易会话ID")
                //如果没有设置签名后的字符串，可以设置私钥，自动完成签名
                .setSign("")
                .setPrivateKey(ConstantValue.WX_API_KEY)
                .create();
        req.setWechatPayResultCallBack(new WechatPayResultCallBack() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplication(), "支付成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(int errorCode, String message) {
                //错误码参考：https://docs.open.alipay.com/204/105301/
                Toast.makeText(getApplication(), "支付失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplication(), "取消支付", Toast.LENGTH_SHORT).show();
            }
        });
        EasyPay.getInstance().sendPayRequest(req);
```

### 3.2 支付宝支付

```java
        //支付宝请求参数文档https://docs.open.alipay.com/204/105465/
        AliBizContent bizContent = new AliBizContent("订单号", "价格", "商品名称", "交易具体描述信息");
        //PayReq中可根据自己需求设置字段
        AliPayReq req = new AliPayReq.Builder()
                .with(mActivity)
                .setBizContent(bizContent)
                //如果没有设置签名后的字符串，可以设置私钥，自动完成签名
                .setSign("")
                .setPrivateKey(ConstantValue.ALI_PRIVATE_KEY)
                .create();
        req.setResultCallBack(new AliPayResultCallBack() {
            @Override
            public void onSuccess() {
                Toast.makeText(getApplication(), "支付成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDealing() {
                Toast.makeText(getApplication(), "支付结果确认中", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String resultStatus, String message) {
                //错误码参考：https://docs.open.alipay.com/204/105301/
                Toast.makeText(getApplication(), "支付失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplication(), "取消支付", Toast.LENGTH_SHORT).show();
            }
        });
        EasyPay.getInstance().sendPayRequest(req);
```


