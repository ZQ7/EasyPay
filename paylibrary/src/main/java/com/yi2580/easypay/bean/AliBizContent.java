package com.yi2580.easypay.bean;

/**
 * @author zhangqi
 * Date:2019/3/9
 * Description:支付宝支付业务参数
 */
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

    public AliBizContent() {
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public AliBizContent(String out_trade_no,String total_amount, String subject, String body) {
        this.out_trade_no = out_trade_no;
        this.total_amount = total_amount;
        this.subject = subject;
        this.body = body;
    }


    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTimeout_express() {
        return timeout_express;
    }

    public void setTimeout_express(String timeout_express) {
        this.timeout_express = timeout_express;
    }


    public String getGoods_type() {
        return String.valueOf(goods_type);
    }

    public void setGoods_type(int goods_type) {
        this.goods_type = goods_type;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("AliBizContent{");
        sb.append("out_trade_no='").append(out_trade_no).append('\'');
        sb.append(", total_amount='").append(total_amount).append('\'');
        sb.append(", subject='").append(subject).append('\'');
        sb.append(", body='").append(body).append('\'');
        sb.append(", timeout_express='").append(timeout_express).append('\'');
        sb.append(", product_code='").append(product_code).append('\'');
        sb.append(", goods_type=").append(goods_type);
        sb.append('}');
        return sb.toString();
    }

    public String toJson(){
        final StringBuffer sb = new StringBuffer("{");
        sb.append("\"out_trade_no\"").append(":\"").append(out_trade_no).append("\",");
        sb.append("\"total_amount\"").append(":\"").append(total_amount).append("\",");
        sb.append("\"subject\"").append(":\"").append(subject).append("\",");
        sb.append("\"body\"").append(":\"").append(body).append("\",");
        sb.append("\"timeout_express\"").append(":\"").append(timeout_express).append("\",");
        sb.append("\"product_code\"").append(":\"").append(product_code).append("\",");
        sb.append("\"goods_type\"").append(":\"").append(goods_type).append("\"");
        sb.append('}');
        return sb.toString();
    }
}
