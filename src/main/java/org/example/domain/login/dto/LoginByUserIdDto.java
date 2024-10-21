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
public class LoginByUserIdDto {

    //用户编号
    @NotBlank(message = "用户编号不能为空")
    private String userId;

    //明文密码
    @NotBlank(message = "密码不能为空")
    private String pd;

    @NotBlank(message = "图形验证码不能为空")
    private String pictureCode;

    private String sysFlag;
}
