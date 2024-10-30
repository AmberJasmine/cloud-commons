package org.example.domain.file.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Create by Administrator
 * Data 15:20 2021/10/3 星期日
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StuDto {

//    @NotBlank(message = "主鍵不能為空!!!")
    private String id;
    @NotBlank(message = "name不能為空!!!")
    private String name;
    private String gender;

    private LocalDate birthday;

    private String createBy;
    private LocalDateTime createTime;


    private String updateBy;
    private LocalDateTime updateTime;


}
