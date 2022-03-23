package com.example.newtest.service.config;


import com.example.newtest.service.code.MyCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.stereotype.Component;

/**
 * 生产者
 * @author gc
 */
@Slf4j
@Component
public class Producer {
    private String producerGroup = "rocketGroup";
    private DefaultMQProducer defaultMQProducer;


    public Producer(){
        defaultMQProducer = new DefaultMQProducer(producerGroup);
        // 不开启vip通道
        defaultMQProducer.setVipChannelEnabled(false);
        // 绑定命名服务
        defaultMQProducer.setNamesrvAddr(MyCode.NAME_SERVER);
        try {
            // 调用前初始化一次，只能调用一次
            defaultMQProducer.start();
            log.error("默认my生产者启动成功");
        } catch (MQClientException e) {
            log.error("默认my生产者启动失败");
            e.printStackTrace();
        }
    }

    public DefaultMQProducer getDefaultMQProducer(){
        return this.defaultMQProducer;
    }




}
