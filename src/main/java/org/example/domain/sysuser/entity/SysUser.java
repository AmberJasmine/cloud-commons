package org.example.domain.sysuser.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * (SysUser)表实体类
 *
 * @author makejava
 * @since 2022-07-29 08:06:28
 */
@Data
@TableName("sys_user")
@SuppressWarnings("serial")
public class SysUser extends Model<SysUser> {
    //主键
    private String id;
    //用户编号
    private String userId;
    //用户名称
    private String userName;
    //密码
    private String password;
    //明文密码
    private String pd;
    //类型，0普通用户1系统管理员
    private String type;
    //创建人
    private String createUser;
    //创建时间
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    //更新人
    private String updateUser;
    //更新时间
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    //状态，0正常1禁用
    private String status;

    private String phone;

    //头像
    private String profilePhoto;

}