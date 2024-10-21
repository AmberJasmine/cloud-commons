package org.example.domain.common.token.controller;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.common.token.service.TokenService;
import org.example.domain.sysuser.entity.SysUser;
import org.example.domain.sysuser.service.SysUserService;
import org.example.until.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("token")
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private SysUserService sysUserService;

    @Value(value = "${md5-key}")
    public String md5Key;

    @Value("${token.expires}")
    private Integer expires;

    @PostMapping("/gain-token")
    @ApiOperation(value = "获取token", notes = "获取token", httpMethod = "POST")
    public Result<String> collect(
            @RequestParam @NotBlank(message = "账户不能为空") String userId,
            @RequestParam @NotBlank(message = "密码不能为空") String password
    ) throws Exception {
        SysUser sysUser = this.sysUserService.userByUP(userId,password);
        if(Objects.isNull(sysUser)){
            return Result.ofFail("用户名密码不正确");
        }
        String token = this.tokenService.getToken(sysUser);
        return Result.ofSuccess("token获取成功，有效期"+expires+"分钟",token);
    }
}
