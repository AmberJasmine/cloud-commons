package org.example.domain.filecollect.controller;


import com.baomidou.mybatisplus.extension.api.ApiController;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.filecollect.service.FileCollectService;
import org.example.until.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequestMapping("fileCollect")
public class FileCollectController extends ApiController {

    @Autowired
    private FileCollectService fileCollectService;

    @GetMapping("/collect")
    @ApiOperation(value = "收藏", notes = "收藏", httpMethod = "GET")
    public Result<String> collect(
            @ApiParam(value = "文件id", required = true)
            @RequestParam String id
    ) {
        return this.fileCollectService.collectFile(id);
    }
}

