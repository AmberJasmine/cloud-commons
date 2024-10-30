package org.example.domain.file.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Create by Administrator
 * Data 15:20 2021/10/3 星期日
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadFileRelQueryDto {

    private String id;
    @NotEmpty(message = "关联id不能为空")
    private List<String> masterIds;
    private String fileId;
    @NotBlank(message = "所属模块不能为空")
    private String belongModel;

    private String createUser;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    private String updateUser;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    private String deleted;

    private Long pageCurrent;
    private Long pageSize;

    private String order;
    private String prop;

}
