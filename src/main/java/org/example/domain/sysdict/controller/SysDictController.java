package org.example.domain.sysdict.controller;


import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.example.config.WhiteListConfig;
import org.example.domain.sysdict.dto.SysDictDto;
import org.example.domain.sysdict.service.SysDictService;
import org.example.domain.sysdict.vo.SysDictVo;
import org.example.until.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = {"数据字典"})
@Slf4j
@RestController
@RequestMapping("sys-dict")
public class SysDictController extends ApiController {

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private WhiteListConfig whiteListConfig;

    @ApiOperation(value = "条件查询分页", notes = "条件查询分页", httpMethod = "POST")
    @PostMapping("/page")
    public Result<Page<SysDictVo>> getPage(@RequestBody SysDictDto dto) {
        List<String> interfaceList =
                this.whiteListConfig.getInterfaceList();
        System.out.println("interfaceList = " + interfaceList);

        try {
            return this.sysDictService.getPage(dto);
        } catch (Exception e) {
            log.info("查询字典失败,错误信息：{}", e.getMessage());
            return Result.ofFail("查询字典失败,错误信息" + e.getMessage());
        }
    }

    @ApiOperation(value = "新增字典", notes = "新增字典", httpMethod = "POST")
    @PostMapping("/add")
    public Result<SysDictVo> add(@RequestBody SysDictDto dto) {
        try {
            return this.sysDictService.add(dto);
        } catch (Exception e) {
            log.info("新增字典失败,错误信息：{}", e.getMessage());
            return Result.ofFail("新增字典失败,错误信息" + e.getMessage());
        }
    }

    @ApiOperation(value = "更新字典", notes = "更新字典", httpMethod = "POST")
    @PostMapping("/update")
    public Result<SysDictVo> update(@RequestBody SysDictDto dto) {
        try {
            return this.sysDictService.updateOne(dto);
        } catch (Exception e) {
            log.info("新增字典失败,错误信息：{}", e.getMessage());
            return Result.ofFail("新增字典失败,错误信息" + e.getMessage());
        }
    }

    @ApiOperation(value = "字典详情", notes = "字典详情", httpMethod = "POST")
    @PostMapping("/detail")
    public Result<SysDictVo> detail(@RequestBody SysDictDto dto) {
        try {
            return this.sysDictService.detail(dto);
        } catch (Exception e) {
            log.info("新增字典失败,错误信息：{}", e.getMessage());
            return Result.ofFail("新增字典失败,错误信息" + e.getMessage());
        }
    }

    @ApiOperation(value = "删除字典", notes = "删除字典", httpMethod = "POST")
    @PostMapping("/delete")
    public Result<String> delete(@RequestBody SysDictDto dto) {
        try {
            return this.sysDictService.delete(dto);
        } catch (Exception e) {
            log.info("新增字典失败,错误信息：{}", e.getMessage());
            return Result.ofFail("新增字典失败,错误信息" + e.getMessage());
        }
    }


}

