package org.example.domain.file.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.example.domain.file.dto.StuDto;
import org.example.domain.file.dto.StuPageDto;
import org.example.domain.file.entity.StuEntity;
import org.example.domain.file.service.StuService;
import org.example.until.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Create by Administrator
 * Data 15:25 2021/10/3 星期日
 */
@RestController
@RequestMapping("/demo")
@Api(tags = {"测试mysql-demo"})
@Validated
public class StuController {

    @Autowired
    private StuService stuService;

    @GetMapping("/getStus")
    @ApiOperation(value = "获取stu数据", notes = "notes获取stu", httpMethod = "GET")
    public List<StuEntity> getStus() {
        return stuService.getStus();
    }

    @GetMapping("/deleteOne")
    @ApiOperation(value = "删除数据", notes = "删除数据", httpMethod = "GET")
    public Result<Integer> deleteOne(@RequestParam String id) {
        return stuService.deleteOne(id);
    }

    @GetMapping("/deleteList")
    @ApiOperation(value = "批量删除数据", notes = "批量删除数据", httpMethod = "GET")
    public Result<String> deleteList(@RequestParam @Valid @NotEmpty List<String> list) {
        return stuService.deleteList(list);
    }

    @PostMapping("/getStuPage")
    @ApiOperation(value = "分页数据", notes = "分页数据", httpMethod = "POST")
    public Result<Page<StuEntity>> getStuPage(@RequestBody StuPageDto stuPageDto) {
        return stuService.getStuPAge(stuPageDto);
    }

    @PostMapping("/updateOne")
    @ApiOperation(value = "分页数据", notes = "分页数据", httpMethod = "POST")
    public Result<String> updateOne(@RequestBody StuDto stuDto) throws UnknownHostException {
        return stuService.updateOne(stuDto);
    }

    @GetMapping("/getAllAdd")
    @ApiOperation(value = "自定义查询全表", notes = "自定义查询全表", httpMethod = "GET")
    public List<StuEntity> getAllAdd() {
        return stuService.selectAllAdd();
    }

    @GetMapping("/getfi")
    @ApiOperation(value = "get", notes = "get", httpMethod = "GET")
    public List<StuEntity> getfi(@RequestParam(value = "name", required = false)
                                         String name) {
        return stuService.getStus1(name);
    }

    @GetMapping("/getAll")
    @ApiOperation(value = "getAll", notes = "getAll", httpMethod = "GET")
    public List<StuEntity> getAll(@RequestParam(value = "idList", required = true)
                                  @Valid @NotEmpty List<String> idList) {
        return stuService.getAll(idList);
    }


    @PostMapping("/addOne")
    @ApiOperation(value = "addOne", notes = "addOne", httpMethod = "POST")
    public Result<Integer> addOne(
            @ApiParam(value = "入参", name = "入参stuDto", required = true)
            @Valid
            @NotNull(message = "not null?")
            @RequestBody StuDto stuDto) {
        return stuService.addOne(stuDto);
    }
}
