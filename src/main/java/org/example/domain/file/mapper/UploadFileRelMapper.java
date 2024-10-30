package org.example.domain.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.domain.file.dto.UploadFileRelQueryDto;
import org.example.domain.file.entity.UploadFileRelEntity;
import org.example.domain.file.vo.UploadFileEntityVo;

import java.util.List;

/**
 * Create by Administrator
 * Data 15:22 2021/10/3 星期日
 */
@Mapper
public interface UploadFileRelMapper extends BaseMapper<UploadFileRelEntity> {

    List<UploadFileEntityVo> pageByMasterId(@Param("queryDto") UploadFileRelQueryDto queryDto);

}
