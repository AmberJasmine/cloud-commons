package org.example.domain.sysdict.controller;


import com.baomidou.mybatisplus.extension.api.ApiController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.sysdict.dto.SysDictDataDto;
import org.example.domain.sysdict.service.SysDictDataService;
import org.example.domain.sysdict.vo.SysDictDataVo;
import org.example.until.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Api(tags = {"数据字典项"})
@Slf4j
@RestController
@RequestMapping("sys-dict-data")
public class SysDictDataController extends ApiController {

    @Autowired
    private SysDictDataService sysDictDataService;

    @ApiOperation(value = "查询字典内容", notes = "查询字典内容", httpMethod = "POST")
    @PostMapping("/listByDictId")
    public Result<List<SysDictDataVo>> listByDictId(@RequestParam String dictId,
                                                    @RequestParam(defaultValue = "false") Boolean forbidden) {
        try {
            return this.sysDictDataService.listByDictId(dictId, forbidden);
        } catch (Exception e) {
            log.info("查询字典失败,错误信息：{}", e.getMessage());
            return Result.ofFail("查询字典失败,错误信息" + e.getMessage());
        }
    }

    @ApiOperation(value = "启用，停用字典内容", notes = "启用，停用字典内容", httpMethod = "POST")
    @PostMapping("/able")
    public Result<String> able(@RequestParam String id) {
        try {
            return this.sysDictDataService.able(id);
        } catch (Exception e) {
            log.info("启用，停用字典内容失败,错误信息：{}", e.getMessage());
            return Result.ofFail("启用，停用字典内容,错误信息" + e.getMessage());
        }
    }


    @ApiOperation(value = "新增字典项", notes = "新增字典项", httpMethod = "POST")
    @PostMapping("/add")
    public Result add(@RequestBody @Valid @NotNull SysDictDataDto dto) {
        try {
            return this.sysDictDataService.add(dto);
        } catch (Exception e) {
            log.info("字典项新增失败,错误信息：{}", e.getMessage());
            return Result.ofFail("字典项新增失败,错误信息" + e.getMessage());
        }
    }

    @ApiOperation(value = "更新字典项", notes = "更新字典项", httpMethod = "POST")
    @PostMapping("/update")
    public Result update(@RequestBody SysDictDataDto dto) {
        try {
            return this.sysDictDataService.updateOne(dto);
        } catch (Exception e) {
            log.info("字典项更新失败,错误信息：{}", e.getMessage());
            return Result.ofFail("字典项更新失败,错误信息" + e.getMessage());
        }
    }

    @ApiOperation(value = "删除字典项", notes = "删除字典项", httpMethod = "POST")
    @PostMapping("/delete")
    public Result delete(@RequestParam @Valid @NotBlank(message = "字典项id不能为空") String id) {
        try {
            return this.sysDictDataService.delete(id);
        } catch (Exception e) {
            log.info("字典项删除失败,错误信息：{}", e.getMessage());
            return Result.ofFail("字典项删除失败,错误信息" + e.getMessage());
        }
    }

}

