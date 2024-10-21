package org.example.domain.login.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginByPhoneDto {

    @NotBlank(message = "手机号码不能为空")
//    @Pattern(regexp = "/^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$/"
//            , message = "手机号码不正确")
    private String phone;
    @NotBlank(message = "短信验证码不能为空")
    private String messageCode;

}
