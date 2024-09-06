package org.example.domain.sysdict.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.sysdict.dto.SysDictDto;
import org.example.domain.sysdict.entity.SysDict;
import org.example.domain.sysdict.vo.SysDictVo;
import org.example.until.Result;

public interface SysDictService extends IService<SysDict> {

    Result<Page<SysDictVo>> getPage(SysDictDto dto);

    Result<SysDictVo> add(SysDictDto dto);

    Result<SysDictVo> detail(SysDictDto dto);

    Result<String> delete(SysDictDto dto);

    Result<SysDictVo> updateOne(SysDictDto dto);
}

