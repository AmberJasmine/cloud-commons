package org.example.feign.controller;

import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.transform.Result;

/**
 * Create by Administrator
 * Data 12:01 2021/12/16 星期四
 */
@Api(tags = "基础工具")
@RestController
@RequestMapping("commons")
public class ProviderCommonsController {
    @GetMapping("/common")
    public String common() {
        return "commons";
    }

}
