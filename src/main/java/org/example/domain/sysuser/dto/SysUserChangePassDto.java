package org.example.domain.sysuser.dto;


import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;


@Data
@Validated
public class SysUserChangePassDto {

    @NotBlank(message = "用户编号不能为空")
    private String userId;

    @NotBlank(message = "旧密码不能为空")
    private String oldPd;

    @NotBlank(message = "新密码不能为空")
    private String pd;

    @NotBlank(message = "确认新密码不能为空")
    private String checkPd;

}