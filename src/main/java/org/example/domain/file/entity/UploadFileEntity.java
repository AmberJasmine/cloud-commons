package org.example.domain.file.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Create by Administrator
 * Data 15:20 2021/10/3 星期日
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("upload_file")
public class UploadFileEntity {

    @TableId("ID")
    private String id;
    private String equip;
    private String directory;
    private String fileName;
    private String fileType;
    private String fileSize;
    private String uuid;
    private String storageName;
    private String createBy;
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    private String realUrl;
    private String previewUrl;
    private String deleted;

}
