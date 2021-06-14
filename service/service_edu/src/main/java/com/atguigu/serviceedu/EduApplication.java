package com.atguigu.serviceedu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//启动类
@EnableDiscoveryClient   //nacos注册
@EnableFeignClients     //调用端
@SpringBootApplication
@ComponentScan(basePackages = {"com.atguigu"})
public class EduApplication {
    public static void main(String[] args){
        SpringApplication.run(EduApplication.class, args);
    }
}
