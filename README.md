# EasyPay

[ [![](https://jitpack.io/v/ZQ7/EasyPay.svg)](https://jitpack.io/#ZQ7/EasyPay) ](https://jitpack.io/#ZQ7/EasyPay)

> 对微信支付和支付宝支付的App端SDK进行二次封装，对外提供一个较为简单的接口和支付结果回调


## 相关文档
1. 支付宝APP支付文档：
https://docs.open.alipay.com/204

2. 微信APP支付文档：
https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=8_1

## 1. 如何添加

#### 在app目录下的build.gradle中添加依赖(微信支付宝SDK版本号请查询文档自行添加)

```gradle
implementation 'com.github.ZQ7:EasyPay::1.1.0'
```

## 2. Android Manifest配置

**注册activity**

```xml
        <!-- 微信支付(不需要创建WXPayEntryActivity) -->
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

### 3.1 微信支付(包含部分参数简介)

```java
        //微信请求参数文档https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=9_12&index=2
        WechatPayReq req = new WechatPayReq.Builder()
                .with(mActivity)
                .setPrepayId("预支付交易会话ID")
                //如果没有设置签名后的字符串，可以设置私钥，自动完成签名(签名一般由服务端完成)
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

```java
{
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
}

```

### 3.2 支付宝支付(包含部分参数简介)

```java
        //支付宝请求参数文档https://docs.open.alipay.com/204/105465/
        AliBizContent bizContent = new AliBizContent("订单号", "价格", "商品名称", "交易具体描述信息");
        //PayReq中可根据自己需求设置字段
        AliPayReq req = new AliPayReq.Builder()
                .with(mActivity)
                .setBizContent(bizContent)
                //如果没有设置签名后的字符串，可以设置私钥，自动完成签名(签名一般由服务端完成)
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


```java
{
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
        private String bizContentStr;
        
        
    public class AliBizContent {

    /**
     * 商户网站唯一订单号
     */
    private String out_trade_no;

    /**
     * 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]
     */
    private String total_amount;

    /**
     * 商品的标题/交易标题/订单标题/订单关键字等。
     */
    private String subject;

    /**
     * 对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body。
     */
    private String body;

    /**
     * 该笔订单允许的最晚付款时间，逾期将关闭交易。
     * 取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。
     * 该参数数值不接受小数点， 如 1.5h，可转换为 90m。
     * 注：若为空，则默认为15d。
     */
    private String timeout_express = "15d";

    /**
     * 销售产品码，商家和支付宝签约的产品码，为固定值QUICK_MSECURITY_PAY
     */
    private String product_code = "QUICK_MSECURITY_PAY";

    /**
     * 商品主类型：0—虚拟类商品，1—实物类商品
     * 注：虚拟类商品不支持使用花呗渠道
     */
    private int goods_type;
    
    }
}
        
```


