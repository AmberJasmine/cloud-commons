package org.example.domain.systagmanage.dto;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 标签表(SysTagManage)表实体类
 *
 * @author makejava
 * @since 2022-08-14 12:42:00
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("serial")
public class LoginInfoDto {

    @ApiModelProperty(value = "ip")
    private String ip;

    @ApiModelProperty(value = "name")
    private String name;

    @ApiModelProperty(value = "ipStr")
    private String ipStr;


}

