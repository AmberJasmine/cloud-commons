package org.example.domain.file.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;

/**
 * Create by Administrator
 * Data 16:43 2021/12/12 星期日
 */
@Data
@Builder
public class StuPageDto {

    @Min(value = 1, message = "当前页最小值1")
    private Integer pageCurrent;

    @Min(value = 1,message = "页面容量最小值1")
    private Integer pageSize;

    private String name;

}
