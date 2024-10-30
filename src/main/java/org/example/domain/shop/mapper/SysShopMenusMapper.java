package org.example.domain.shop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
//import com.example.pags.mysql.demo.sys.shop.entity.SysShopMenus;
import org.apache.ibatis.annotations.Mapper;
import org.example.domain.shop.entity.SysShopMenus;

import java.util.List;


@Mapper
public interface SysShopMenusMapper extends BaseMapper<SysShopMenus> {
    List<SysShopMenus> nodeChildIds(String id);
}
