package org.example.base.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Create by Administrator
 * Data 12:01 2021/12/16 星期四
 */
@Api(tags = "基础工具")
@RestController
@RequestMapping("commons")
public class ProviderCommonsController {

    @Value(value = "${server.port}")
    public String port;

    @Value(value = "${spring.application.name}")
    public String springName;

    @GetMapping("/hello/{name}")
    @ApiOperation(value = "commons测试", notes = "cloud-commons", httpMethod = "GET")
    public String hello(@PathVariable String name) {
        return springName + ":" + port + " || hello :" + name;
    }

}
