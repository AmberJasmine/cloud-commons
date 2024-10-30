package org.example.domain.shop.controller;


import com.baomidou.mybatisplus.extension.api.ApiController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.shop.dto.SysShopMenusDto;
import org.example.domain.shop.entity.SysShopMenus;
import org.example.domain.shop.service.SysShopMenusService;
import org.example.until.Result;
import org.example.until.tree.TreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Objects;

@Api(tags = {"菜单"})
@Slf4j
@Validated
@RestController
@RequestMapping("shop")
public class SysShopMenusController extends ApiController {

    @Autowired
    private SysShopMenusService sysShopMenusService;

    @GetMapping("/node-info")
    @ApiOperation(value = "节点下的数据（该节点的id做父节点查询）", notes = "节点下的数据（该节点的id做父节点查询）", httpMethod = "GET")
    public Result<List<SysShopMenus>> collect(
            @ApiParam(value = "id", required = true)
            @RequestParam String id
    ) {
        return this.sysShopMenusService.node(id);
    }

    @PostMapping("/menu")
    @ApiOperation(value = "租户菜单(树状结构)", notes = "租户菜单(树状结构)", httpMethod = "POST")
    public Result<List<TreeNode>> menu(
            @ApiParam(value = "tentId", required = true)
            @RequestParam String tentId
    ) {
        return this.sysShopMenusService.menu(tentId);
    }

    @GetMapping("/childTree")
    @ApiOperation(value = "该节点树状结构", notes = "该节点树状结构", httpMethod = "GET")
    public Result<TreeNode> childTree(
            @ApiParam(value = "id", required = true)
            @RequestParam String id
    ) {
        return this.sysShopMenusService.childTree(id);
    }

    @GetMapping("/childIds")
    @ApiOperation(value = "该节点子节点ids(bool为true，包含该节点)", notes = "该节点子节点ids（bool为true，包含该节点）", httpMethod = "GET")
    public Result<List<SysShopMenus>> childIds(
            @ApiParam(value = "id", required = true)
            @RequestParam String id,
            @ApiParam(value = "bool", required = true)
            @RequestParam Boolean bool
    ) {
        return this.sysShopMenusService.childIds(id, bool);
    }

    @PostMapping("/down")
    @ApiOperation(value = "下载资源", notes = "下载资源", httpMethod = "POST")
    public void down(HttpServletResponse response) {
        String filePath = Objects.requireNonNull(this.getClass().getClassLoader()
                .getResource("templates/GitUser.xlsx")).getPath().substring(1);
        log.info("filePath = {}", filePath);
        File file = new File(filePath);
        if (file.exists()) {
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                response.setContentType("application/force-download");
                response.setHeader("Content-Disposition", "attachment;fileName="
                        + URLEncoder.encode("下载+_ _excel.xlsx", "UTF-8")
                        .replace("+", "%20")
                );
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                ServletOutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @PostMapping("/add")
    @ApiOperation(value = "添加菜单", notes = "添加菜单", httpMethod = "POST")
    public Result add(@RequestBody @Valid SysShopMenusDto sysShopMenusDto) {
        return this.sysShopMenusService.add(sysShopMenusDto);
    }

    @PostMapping("/delete")
    @ApiOperation(value = "删除菜单", notes = "删除菜单", httpMethod = "POST")
    public Result delete(@RequestParam @Valid @NotEmpty(message = "删除的信息不能为空") List<String> list) {
        return this.sysShopMenusService.delete(list);
    }

    @PostMapping("/shopping")
    @ApiOperation(value = "已开店的租户", notes = "已开店的租户", httpMethod = "POST")
    public Result shopping() {
        return this.sysShopMenusService.shopping();
    }

}

