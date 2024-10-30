package org.example.domain.systagmanage.entity;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 标签表(SysTagManage)表实体类
 *
 * @author makejava
 * @since 2022-08-14 12:42:00
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "sys_tag_manage")
@SuppressWarnings("serial")
public class SysTagManage extends Model<SysTagManage> {
    //id
    @TableId
    private String id;
    //标签名称
    @TableField(value = "tag_name")
    private String tagName;
    //note,other
    @TableField(value = "tag_type")
    private String tagType;
    //用户id
    @TableField(value = "user_id")
    private String userId;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}

