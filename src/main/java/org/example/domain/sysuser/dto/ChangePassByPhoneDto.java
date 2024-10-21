package org.example.domain.sysuser.dto;


import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;


@Data
@Validated
public class ChangePassByPhoneDto {

    @NotBlank(message = "手机号码不能为空")
    private String phone;

    @NotBlank(message = "新密码不能为空")
    private String pd;

    @NotBlank(message = "确认新密码不能为空")
    private String checkPd;

}