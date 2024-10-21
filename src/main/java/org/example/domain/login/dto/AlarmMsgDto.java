package org.example.domain.login.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmMsgDto {

    @NotBlank(message = "手机号不能为空")
    private String phone;

    @NotBlank(message = "短信内容不能为空")
    private String message;

    // 提前时间
    @NotNull(message = "提前时间不能为空")
    private Long aheadTime;
    // 日期
    @NotBlank(message = "日期不能为空")
    private String date;
    // 节次
    @NotBlank(message = "节次不能为空")
    private String num;

}
