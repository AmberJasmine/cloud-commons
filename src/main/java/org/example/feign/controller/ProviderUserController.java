package org.example.feign.controller;

import io.swagger.annotations.Api;
import org.example.feign.interfaces.ProviderUserFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Create by Administrator
 * Data 12:01 2021/12/16 星期四
 */
@Api(tags = "Feign测试")
@RestController
@RequestMapping("user")
public class ProviderUserController {

    @Autowired
    private ProviderUserFeign providerUserFeign;

    @GetMapping("/hi")
    public String hi(@RequestParam String name){
        return providerUserFeign.hi(name);
    }

}
