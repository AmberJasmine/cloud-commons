package org.example.domain.file.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Create by Administrator
 * Data 15:20 2021/10/3 星期日
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UploadFileDto {

    private String equip;
    private String directory;
    private String uuid;
    private String fileName;
    private String fileType;
    private String createBy;
    private String realUrl;
    private String previewUrl;


}
