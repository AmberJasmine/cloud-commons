package org.example.domain.sysuser.dto;


import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.Date;


@Data
@Validated
public class SysUserDto {
    //主键
    private String id;
    //用户编号
    @NotBlank(message = "用户编号不能为空")
    private String userId;
    //用户名称
    private String realUserName;
    //密码
    private String password;
    //明文密码
    @NotBlank(message = "密码不能为空")
    private String pd;
    //类型，0普通用户1系统管理员
    private String type;
    //创建人
    private String createUser;
    //创建时间
    private Date createTime;
    //更新人
    private String updateUser;
    //更新时间
    private Date updateTime;
    //状态，0正常1禁用
    private String status;

    @NotBlank(message = "手机号码不能为空")
//    @Pattern(regexp = "/^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$/"
//            , message = "手机号码不正确")
    private String phone;
    @NotBlank(message = "短信验证码不能为空")
    private String messageCode;

    private String photo;
}