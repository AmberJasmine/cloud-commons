package org.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//@EnableEurekaClient
@EnableSwagger2
@EnableDiscoveryClient
//@EnableFeignClients(basePackages = {"org.example.feign.**"})
//@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@MapperScan(basePackages = {"org.example.**.mapper", "org.example.**.dao"})
@EntityScan(basePackages = {"org.example.**.entity"})
@SpringBootApplication
public class CommonsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CommonsApplication.class, args);
    }

}
