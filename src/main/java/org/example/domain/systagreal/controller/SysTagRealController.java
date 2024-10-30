package org.example.domain.systagreal.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.example.domain.systagreal.service.SysTagRealService;
import org.example.until.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/sysTagReal")
@Api(tags = {"打标"})
@Validated
public class SysTagRealController {

    @Autowired
    private SysTagRealService sysTagRealService;

    @GetMapping("/tagForData")
    @ApiOperation(value = "查询某一天上传的文件", notes = "查询某一天上传的文件", httpMethod = "GET")
    public Result tagForData(
            @ApiParam(value = "数据is")
            @RequestParam String id,
            @ApiParam(value = "标签ids")
            @RequestParam List<String> tagIds
    ) {
        return Result.ofSuccess(this.sysTagRealService.tagForData(id,tagIds));
    }

}
