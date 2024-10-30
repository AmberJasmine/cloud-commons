package org.example.domain.systagmanage.dto;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domain.systagmanage.entity.SysTagManage;

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
public class SysTagManageDto {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "标签名称")
    private String tagName;

    @ApiModelProperty(value = "note,other")
    private String tagType;

    @ApiModelProperty(value = "用户id")
    private String userId;

    private Page<SysTagManage> page;

}

