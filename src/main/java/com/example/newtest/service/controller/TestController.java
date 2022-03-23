package com.example.newtest.service.controller;

import com.example.newtest.service.code.MyCode;
import com.example.newtest.service.config.Producer;
import com.example.newtest.service.javautlis.FileUtils;
import com.example.newtest.service.javautlis.Result;
import com.example.newtest.service.pojo.User;
import com.example.newtest.service.service.TestService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 测试使用
 * @author gc
 */

@Slf4j
@Api(value = "这是入口", description = "这真的是一个入口")
@RestController
@CrossOrigin
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestService testService;

    /*@Autowired
    private RocketMQTemplate rocketMQTemplate;*/

    @Value("${web.upload.linux}")
    private String linuxPath;

    @Value("${web.upload.window}")
    private String windowPath;

    @ApiOperation(value = "这是value", notes = "test1接口")
    @ApiImplicitParam(name = "map", value = "map参数说明", required = false, dataType = "json")
    @PostMapping("/poiTest")
    public Result Test1(
            @RequestBody(required = false) /*@ApiParam(value = "map参数", defaultValue = "map的说明", type = "Map", required = false, name = "这是名字")*/ Map<String, Object> map) {
        return new Result(true, 2000, "成功", map);
    }

    @ApiOperation(value = "这是value", notes = "test1接口")
    @ApiImplicitParam(name = "map", value = "map参数说明", required = false, dataType = "json")
    @GetMapping("/poiTest")
    public Result Test1V2(
            @RequestBody(required = false) /*@ApiParam(value = "map参数", defaultValue = "map的说明", type = "Map", required = false, name = "这是名字")*/ Map<String, Object> map) {
        return new Result(true, 20000, "成功V2", map);
    }

    @ApiOperation(value = "这是value2", notes = "test2接口")
    @ApiImplicitParam(name = "user", value = "user实体", required = true, dataType = "User")
    @PostMapping("/poiTest2")
    public Result Test2(
            @RequestBody User user) {
        return new Result(false, 20001, "error", user);
    }

    @ApiOperation(value = "这是value3", notes = "test3接口")
    @ApiImplicitParam(name = "map名字", value = "map值", required = true, dataType = "Map")
    @PostMapping("/poiTest3")
    public Result Test3(
            @RequestBody Map<String, Object> map) {
        //rocketMQTemplate.convertAndSend("你好啊，我是mq");
        return testService.testService1(map);
    }



    @ApiOperation(value = "这是value4", notes = "test4接口")
    @ApiImplicitParam(name = "map名字", value = "map值", required = true, dataType = "Map")
    @PostMapping("/poiTest4")
    public Result Test4(
            @RequestParam("file") MultipartFile file) {
        // 获取文件名字
        String filename = file.getOriginalFilename();
        FileUtils.upload(file, linuxPath, filename);
        System.out.println("完成");
        return new Result(true, 2000, "ok");
    }


    @ApiOperation(value = "这是value5", notes = "test5接口")
    @GetMapping("/poiTest5")
    public Result Test5(HttpServletResponse response) {
        System.out.println("接口5");
        String path = "D:\\\\data\\\\QQ截图20211102171944.png";
        // 获取文件名字
        String download = FileUtils.download(path, response);
        if (download == null) {
            return new Result(false, 20001, "error");
        }
        System.out.println("完成");
        return new Result(true, 2000, "ok");
    }

    @Autowired
    private Producer producer;

    private List<String> mesList;

    /**
     * 初始化消息
     */
      {
        mesList = new ArrayList<>();
        mesList.add("小小");
        mesList.add("爸爸");
        mesList.add("妈妈");
        mesList.add("爷爷");
        mesList.add("奶奶");

    }

    @RequestMapping("/rocketmq")
    public Object callback() throws Exception {
        //总共发送五次消息
        for (String s : mesList) {
            //创建生产信息,bytes字节
            Message message = new Message(MyCode.TOPIC, "testtag", ("小小一家人的称谓:" + s).getBytes());
            //发送
            SendResult sendResult = producer.getDefaultMQProducer().send(message);
            log.info("输出生产者信息={}",sendResult);
        }
        return "成功";
    }

    @RequestMapping("/rocketmqTest2")
    public Result rocketmqTest2(@RequestBody Map<String, Object> map) throws Exception {
        String meg = (String) map.get("meg");
        Integer type = (Integer) map.get("type");
        Assert.hasText(meg, "参数为空");
        Message message = new Message(MyCode.TOPIC, "testtag", meg.getBytes());
        SendResult send = producer.getDefaultMQProducer().send(message);
        log.info("发信的信息为:{}", meg);
        log.info("消息体为:{}", send);
        if (type == 1) {
            callback();
        }
        return new Result(true, 1, "成功");
    }
}
