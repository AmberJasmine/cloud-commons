package org.example.domain.sysuser.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;


@Data
@Validated
public class SysUserSearchDto {

    @NotNull
    @Min(value = 1)
    private Long pageCurrent = 1L;

    @NotNull
    @Min(value = 1)
    private Long pageSize = 5L;

    //主键
    private String id;
    //用户编号
    private String userId;
    //类型，0普通用户1系统管理员
    private String type;
    //创建人
    private String createUser;
    //创建时间
    private Date createTime;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private String startTime;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private String endTime;
    //更新人
    private String updateUser;
    //更新时间
    private Date updateTime;
    //状态，0正常1禁用
    private String status;

    private String phone;

}