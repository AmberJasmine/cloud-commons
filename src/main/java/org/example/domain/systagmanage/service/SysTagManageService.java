package org.example.domain.systagmanage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.systagmanage.dto.SysTagManageDto;
import org.example.domain.systagmanage.entity.SysTagManage;
import org.example.until.Result;

import java.util.List;

/**
 * 标签表(SysTagManage)表服务接口
 *
 * @author makejava
 * @since 2022-08-14 12:42:01
 */
public interface SysTagManageService extends IService<SysTagManage> {

    Page<SysTagManage> page(SysTagManageDto dto);

    List<SysTagManage> getList(SysTagManageDto dto);

    Result<SysTagManage> addOne(SysTagManage sysTagManage);

    Result<Void> deleteOne(SysTagManage sysTagManage);

}

