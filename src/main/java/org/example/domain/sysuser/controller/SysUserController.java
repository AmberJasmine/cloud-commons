package org.example.domain.sysuser.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.example.domain.sysuser.dto.ChangePassByPhoneDto;
import org.example.domain.sysuser.dto.SysUserChangePassDto;
import org.example.domain.sysuser.dto.SysUserDto;
import org.example.domain.sysuser.dto.SysUserSearchDto;
import org.example.domain.sysuser.entity.SysUser;
import org.example.domain.sysuser.service.SysUserService;
import org.example.until.Result;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * (SysUser)表控制层
 *
 * @author makejava
 * @since 2022-07-29 08:06:24
 */
@RestController
@RequestMapping("sysUser")
public class SysUserController extends ApiController {
    /**
     * 服务对象
     */
    @Resource
    private SysUserService sysUserService;

    /**
     * 分页查询所有数据
     *
     * @param page    分页对象
     * @param sysUser 查询实体
     * @return 所有数据
     */
    @GetMapping
    public R selectAll(Page<SysUser> page, SysUser sysUser) {
        return success(this.sysUserService.page(page, new QueryWrapper<>(sysUser)));
    }

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @return 单条数据
     */
    @GetMapping("{id}")
    public R selectOne(@PathVariable Serializable id) {
        return success(this.sysUserService.getById(id));
    }

    /**
     * 新增数据
     *
     * @param sysUser 实体对象
     * @return 新增结果
     */
    @PostMapping
    public R insert(@RequestBody SysUser sysUser) {
        return success(this.sysUserService.save(sysUser));
    }


    @PostMapping("userByUserId")
    @ApiOperation(value = "查询用户详情", notes = "查询用户详情", httpMethod = "POST")
    public Result userByUserId(@RequestParam @NotBlank(message = "用户编号不能为空") String userId) {
        try {
            return this.sysUserService.userByUserId(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ofFail("失败");
        }
    }

    @PostMapping("updateByUserId")
    @ApiOperation(value = "更新用户信息", notes = "更新用户信息", httpMethod = "POST")
    public Result updateByUserId(@RequestBody SysUserDto sysUserDto) {
        try {
            return this.sysUserService.updateByUserId(sysUserDto);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ofFail("失败");
        }
    }

    @PostMapping("updateRealNameById")
    @ApiOperation(value = "更新用户信息", notes = "更新用户信息", httpMethod = "POST")
    public Result updateRealNameById(@RequestBody SysUserDto sysUserDto) {
        try {
            return this.sysUserService.updateRealNameById(sysUserDto);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ofFail("失败");
        }
    }

    @PostMapping("pages")
    @ApiOperation(value = "分页", notes = "分页", httpMethod = "POST")
    public Result pages(@RequestBody @Valid SysUserSearchDto sysUserSearchDto) {
        try {
            return this.sysUserService.pages(sysUserSearchDto);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ofFail("失败");
        }
    }



    @PostMapping("save")
    @ApiOperation(value = "注册", notes = "注册", httpMethod = "POST")
    public Result save(@RequestBody @Valid SysUserDto sysUserDto) {
        try {
            return this.sysUserService.save(sysUserDto);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ofFail("注册失败");
        }
    }

    @PostMapping("changePassword")
    @ApiOperation(value = "修改密码(根据客户编号)", notes = "修改密码", httpMethod = "POST")
    public Result changePassword(@RequestBody @Valid SysUserChangePassDto dto) {
        try {
            return this.sysUserService.changePassword(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ofFail("失败");
        }
    }

    @PostMapping("changePdByPhone")
    @ApiOperation(value = "修改密码(忘记密码根据手机号验证，修改密码)", notes = "修改密码", httpMethod = "POST")
    public Result changePdByPhone(@RequestBody @Valid ChangePassByPhoneDto dto) {
        try {
            // 修改密码成功后，用户选择登录，需要获取token
            return this.sysUserService.changePdByPhone(dto);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ofFail("失败");
        }
    }




    /**
     * 修改数据
     *
     * @param sysUser 实体对象
     * @return 修改结果
     */
    @PutMapping
    public R update(@RequestBody SysUser sysUser) {
        return success(this.sysUserService.updateById(sysUser));
    }

    /**
     * 删除数据
     *
     * @param idList 主键结合
     * @return 删除结果
     */
    @DeleteMapping
    public R delete(@RequestParam("idList") List<Long> idList) {
        return success(this.sysUserService.removeByIds(idList));
    }
}

