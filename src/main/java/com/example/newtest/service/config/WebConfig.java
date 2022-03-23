package com.example.newtest.service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 配置图片地址访问
 * @author gc
 * @date 2021年11月3日 10:10:46
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${web.upload.linux}")
    private String linuxPath;

    @Value("${web.upload.window}")
    private String windowPath;


    /**
     * linux设置
     * @param registry
     * */
   /* @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/file/**").addResourceLocations("file:" + linuxPath + "/");
    }*/

    /**
     * window设置
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/file/**").addResourceLocations("file:" + windowPath + "/");
    }
}
