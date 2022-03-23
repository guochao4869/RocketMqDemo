package com.example.newtest.service.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.newtest.service.dto.WxPayDto;
import com.example.newtest.service.javautlis.DateUtils;
import com.example.newtest.service.javautlis.Result;
import com.example.newtest.service.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class TestServiceImpl implements TestService {

    @Value("${wx.app.id}")
    private String wxAppId;

    @Value("${applets.app.id}")
    private String appletsAppId;

    @Value("${key.path}")
    private String keyPath;

    @Value("${mch.id}")
    private String mchId;

    @Value("${mch.key}")
    private String mchKey;

    @Value("${wx.notify.url}")
    private String wxNotifyUrl;

    private final static String wxPayUrl = "https://api.mch.weixin.qq.com/v3/pay/transactions/native";
    private final static String currency = "CNY";


    @Override
    public Result testService1(Map<String, Object> map) {
        BigDecimal total = new BigDecimal(Double.parseDouble(map.get("total").toString()));
        WxPayDto wxPayDto = new WxPayDto();
        wxPayDto.setAppId(wxAppId);
        wxPayDto.setMchid(mchId);
        wxPayDto.setDescription("商品描述");
        wxPayDto.setOutTradeNo("00000002");
        // 到期时间给默认的2个小时就行
        wxPayDto.setTimeExpire(null);
        System.out.println(wxNotifyUrl);
        wxPayDto.setNotifyUrl(wxNotifyUrl);
        HashMap<String, Object> hashMap = new HashMap<>(16);
        BigDecimal bigDecimal = new BigDecimal(100);
        hashMap.put("total", bigDecimal.multiply(total));
        hashMap.put("currency", currency);
        wxPayDto.setAmount(hashMap);

        // 构建微信支付的参数
        RestTemplate restTemplate = new RestTemplate();
        JSONObject body = null;
        try {
            body = restTemplate.postForEntity(wxNotifyUrl, wxPayDto, JSONObject.class).getBody();
            log.info("微信支付结果{}", body);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(true, 1, "ok", body);
    }

    @Override
    public Result testService2(Map<String, Object> map) {

        return null;
    }
}
