package com.example.newtest.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * 微信支付dto
 * @author GC
 * @date 2021年11月2日 11:13:08
 */
@Data
public class WxPayDto implements Serializable {
    /**
     * 应用ID
     */
    private String appId;
    /**
     * 直连商户号
     */
    private String mchid;
    /**
     * 商品描述
     */
    private String description;
    /**
     * 商户订单号
     */
    private String outTradeNo;
    /**
     * 交易结束时间
     */
    private String timeExpire;

    /**
     * 附加数据
     */
    private String attach;

    /**
     * 通知地址,回调地址
     */
    private String notifyUrl;
    /**
     * 金额 total 单位为分
     * 货比类型 currency 人民币为CNY
     */
    Map<String, Object> amount;
}
