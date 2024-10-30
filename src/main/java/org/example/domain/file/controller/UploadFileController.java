package org.example.domain.file.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.example.domain.file.dto.UploadFileOutDto;
import org.example.domain.file.service.UploadFileService;
import org.example.until.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;

/**
 * Create by Administrator
 * Data 15:25 2021/10/3 星期日
 */
@RestController
@RequestMapping("/file-info")
@Api(tags = {"上传的文件信息"})
@Validated
public class UploadFileController {

    @Autowired
    private UploadFileService uploadFileService;

//    @UserLoginToken
    @GetMapping("/get-file-of-oneday")
    @ApiOperation(value = "查询某一天上传的文件", notes = "查询某一天上传的文件", httpMethod = "GET")
    public Result<Page<UploadFileOutDto>> getAllAdd(
            @ApiParam(value = "上传文件日期", required = false)
            @RequestParam String directory,
            @Valid @Min(value = 1, message = "当前页最小值1")
            @RequestParam Long pageCurrent,
            @Valid @Min(value = 1, message = "页面大小最小值1")
            @RequestParam Long pageSize,
            @ApiParam(value = "文件模糊查找值")
            @RequestParam(required = false) String searchName,
            @ApiParam(value = "上传人")
            @RequestParam(required = false) String createBy,
            @ApiParam(value = "标签", required = false)
            @RequestParam List<String> tagIdList
    ) {
        return uploadFileService.getList(directory, pageCurrent, pageSize, tagIdList, searchName, createBy);
    }

    @GetMapping("/deleteById")
    @ApiOperation(value = "删除某条数据", notes = "删除某条数据", httpMethod = "GET")
    public Result<Void> deleteById(
            @ApiParam(value = "id") @RequestParam String id) {
        return this.uploadFileService.deleteById(id);
    }

}
