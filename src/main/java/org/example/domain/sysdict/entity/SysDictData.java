package org.example.domain.sysdict.entity;


import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "sys_dict_data")
public class SysDictData {

    private String id;
    private String dictId;
    @TableField(value = "`key`")
    private String key;
    private String value;
    private Integer sort;
    private String status;
    private String remark;
    private String createBy;
    private Timestamp createTime;
    private String updateBy;
    private Timestamp updateTime;
}

