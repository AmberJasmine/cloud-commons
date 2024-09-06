package org.example.domain.sysdict.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;


@Validated
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysDictDataDto {

    private String id;

    @NotBlank(message = "字典类别不能为空")
    private String dictId;

    @NotBlank(message = "字典代码不能为空")
    private String key;

    private String keyOld;

    @NotBlank(message = "字典名称不能为空")
    private String value;

    private Integer sort;

    private String status;

    @NotBlank(message = "备注不能为空")
    private String remark;

    private String createBy;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp createTime;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp startDate;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp endDate;
    private String updateBy;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Timestamp updateTime;
}

