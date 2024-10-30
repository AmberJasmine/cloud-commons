package org.example.domain.file.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class UploadFileOutDto {

    private String id;
    private String equip;
    private String directory;
    private String uuid;
    private String fileName;
    private String fileType;
    private String createBy;

    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    private String realUrl;
    private String previewUrl;
    private List<String> tagsList;
    private List<String> tagsIdList;

    @ApiModelProperty(value = "是否收藏")
    private Boolean isCollect;
    private Integer collectCount;
}
