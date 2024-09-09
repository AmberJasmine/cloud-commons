package org.example.base.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.example.until.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Create by Administrator
 * Data 12:01 2021/12/16 星期四
 */
@Slf4j
@Api(tags = "基础工具")
@RestController
@RequestMapping("commons")
public class ProviderCommonsController {

    @Autowired
    private LogUtils logUtils;

    @GetMapping("/hello/{name}")
    @ApiOperation(value = "commons测试", notes = "cloud-commons", httpMethod = "GET")
    public String hello(@PathVariable String name) {
        String logs = logUtils.log();
        log.info("服务信息 = {}",logs);
        return logs + " ||>| hello :" + name;
    }

}
