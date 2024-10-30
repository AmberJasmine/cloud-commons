package org.example.domain.file.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.example.domain.file.dto.UploadFileRelQueryDto;
import org.example.domain.file.service.UploadFileRelService;
import org.example.domain.file.vo.UploadFileEntityVo;
import org.example.until.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * Create by Administrator
 * Data 15:25 2021/10/3 星期日
 */
@RestController
@RequestMapping("/fileByMasterId")
@Api(tags = {"masterId下的文件"})
@Validated
public class UploadFileRelController {

    @Autowired
    private UploadFileRelService uploadFileRelService;

    //    @UserLoginToken
    @PostMapping("/page")
    @ApiOperation(value = "masterId下的文件", notes = "masterId下的文件", httpMethod = "POST")
    public Result<Page<UploadFileEntityVo>> page(@RequestBody @Valid @NotNull(message = "参数不能为空") UploadFileRelQueryDto queryDto) {
        Page<UploadFileEntityVo> page = this.uploadFileRelService.listByMaster(queryDto);
        return Result.ofSuccess("查询成功", page);
    }

    @PostMapping("/pages")
    @ApiOperation(value = "masterId下的文件s", notes = "masterId下的文件s", httpMethod = "POST")
    public Result<PageInfo<UploadFileEntityVo>> pages(@RequestBody @Valid @NotNull(message = "参数不能为空") UploadFileRelQueryDto queryDto) {
        PageInfo<UploadFileEntityVo> pageInfo = this.uploadFileRelService.listByMasters(queryDto);
        return Result.ofSuccess("查询成功", pageInfo);
    }

    @PostMapping("/del")
    @ApiOperation(value = "删除附件", notes = "删除附件", httpMethod = "POST")
    public Result<Void> del(@RequestBody UploadFileRelQueryDto queryDto) {
        return this.uploadFileRelService.del(queryDto);
    }

}
