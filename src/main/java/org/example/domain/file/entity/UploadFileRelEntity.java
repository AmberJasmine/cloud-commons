package org.example.domain.file.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("upload_file_rel")
public class UploadFileRelEntity {

    @TableId("ID")
    private String id;
    private String masterId;
    private String fileId;
    private String belongModel;
    private String createUser;
    private LocalDateTime createTime;
    private String updateUser;
    private LocalDateTime updateTime;
    private String deleted;

}
