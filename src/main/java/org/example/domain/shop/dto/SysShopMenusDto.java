package org.example.domain.shop.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysShopMenusDto {

    private String id;

    @NotBlank(message = "菜单名称不能为空")
    private String name;

    @JsonProperty("pId")
    @NotBlank(message = "父id不能为空")
    private String pId;

    private Integer sort;

    @NotBlank(message = "租户不能为空")
    private String tentId;

    private String createBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    private String updateBy;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    private String deleted;

}

