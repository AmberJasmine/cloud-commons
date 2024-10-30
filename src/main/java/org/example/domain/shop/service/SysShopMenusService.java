package org.example.domain.shop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.shop.dto.SysShopMenusDto;
import org.example.domain.shop.entity.SysShopMenus;
import org.example.until.Result;
import org.example.until.tree.TreeNode;
//import com.example.pags.mysql.demo.sys.shop.dto.SysShopMenusDto;
//import com.example.pags.mysql.demo.sys.shop.entity.SysShopMenus;
//import com.example.pags.until.Result;
//import com.example.pags.until.tree.TreeNode;

import java.util.List;


public interface SysShopMenusService extends IService<SysShopMenus> {

    Result<List<SysShopMenus>> node(String id);

    Result add(SysShopMenusDto sysShopMenusDto);

    Result<List<TreeNode>> menu(String tentId);

    Result delete(List<String> list);

    Result shopping();

    Result<TreeNode> childTree(String id);

    Result<List<SysShopMenus>> childIds(String id, Boolean bool);
}

