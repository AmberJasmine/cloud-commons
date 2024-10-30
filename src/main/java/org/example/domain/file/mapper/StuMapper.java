package org.example.domain.file.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.domain.file.entity.StuEntity;

import java.util.List;

/**
 * Create by Administrator
 * Data 15:22 2021/10/3 星期日
 */
@Mapper
public interface StuMapper extends BaseMapper<StuEntity> {

    List<StuEntity> selectAllAdd();

    List<StuEntity> getAll(@Param("idList") List<String> idList);
}
