package org.example.domain.shop.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "sys_shop_menus")
@SuppressWarnings("serial")
public class SysShopMenus {

    @TableId
    private String id;

    @TableField(value = "name")
    private String name;

    @TableField(value = "p_id")
    private String pId;

    @TableField(value = "sort")
    private Integer sort;

    @TableField(value = "tent_id")
    private String tentId;

    @TableField(value = "create_by")
    private String createBy;

    @TableField(value = "create_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField(value = "update_by")
    private String updateBy;

    @TableField(value = "update_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @TableField(value = "deleted")
    private String deleted;

}

