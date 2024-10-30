package org.example.domain.systagreal.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "sys_tag_real")
@SuppressWarnings("serial")
public class SysTagReal extends Model<SysTagReal> {

    @TableId
    private String id;

    @TableField(value = "master_id")
    private String masterId;

    @TableField(value = "tag_id")
    private String tagId;

    @TableField(value = "create_time")
    private LocalDateTime createTime;

}

