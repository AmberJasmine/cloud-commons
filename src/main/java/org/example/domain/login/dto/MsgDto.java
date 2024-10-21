package org.example.domain.login.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MsgDto {

    @NotBlank(message = "手机号不能为空")
    private String phone;

    @NotBlank(message = "短信内容不能为空")
    private String message;

}
