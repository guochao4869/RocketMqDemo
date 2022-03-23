package com.example.newtest.service.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.example.newtest.service.code.MyCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消费者
 * @author gc
 */
@Component
@Slf4j
public class Consumer {

    DefaultMQPushConsumer defaultMQPushConsumer;

    private String consumerGroup = "rocketGroup";

    public Consumer() throws Exception{
        defaultMQPushConsumer = new DefaultMQPushConsumer();
        defaultMQPushConsumer.setConsumerGroup(consumerGroup);
        defaultMQPushConsumer.setNamesrvAddr(MyCode.NAME_SERVER);
        defaultMQPushConsumer.subscribe(MyCode.TOPIC, "*");
        // 从上次偏移量开始消费
        defaultMQPushConsumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        defaultMQPushConsumer.setMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                // 一个一个消费
                MessageExt messageExt = list.get(0);
                String msgId = messageExt.getMsgId();
                String topic = messageExt.getTopic();
                String keys = messageExt.getKeys();
                String body = "";
                try {
                    body = new String(messageExt.getBody(), "utf-8");
                    JSONObject o = JSONObject.parseObject(messageExt.getBody(), JSONObject.class);
                    log.info("=============================================================");
                    log.info("消费者开始消费:{}", o);
                }catch (Exception e) {
                    e.printStackTrace();
                    log.error("字符串转化失败");
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
                log.info("megId为{},topic为{},keys为{},内容为{}", msgId, topic, keys, body);
                // 成功
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        defaultMQPushConsumer.start();
        log.info("消费成功");
    }
}
