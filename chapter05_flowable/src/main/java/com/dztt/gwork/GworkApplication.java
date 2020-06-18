package com.dztt.gwork;

import javax.servlet.MultipartConfigElement;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;

@SpringBootApplication
//添加对mapper包扫描
@MapperScan("com.dztt.gwork.**.mapper")
//过滤器开关
@ServletComponentScan("com.dztt.gwork.utils")
//取消spring security的认证
@EnableAutoConfiguration(exclude = {org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class})
//开启缓存功能
//@EnableCaching
//启动swagger注解
public class GworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(GworkApplication.class, args);


    }
    /**
     * 文件上传配置 zhongzk
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //单个文件最大
        factory.setMaxFileSize("300MB"); //KB,MB
        // 设置总上传数据总大小
        factory.setMaxRequestSize("500MB");
        // 临时文件路径
        String tempUrl = System.getProperty("user.dir") + File.separator + "bjgwork" +  File.separator + "tmp";
        System.out.println("临时目录：" + tempUrl);
        File file = new File(tempUrl);
        if (!file.exists()) {
            file.mkdirs();
        }
        factory.setLocation(tempUrl);
        return factory.createMultipartConfig();
    }
}
