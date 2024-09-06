package org.example.domain.sysdict.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.sysdict.dto.SysDictDataDto;
import org.example.domain.sysdict.entity.SysDictData;
import org.example.domain.sysdict.vo.SysDictDataVo;
import org.example.until.Result;

import java.util.List;

public interface SysDictDataService extends IService<SysDictData> {

    Result<List<SysDictDataVo>> listByDictId(String dictId, boolean forbidden);

    Result<String> able(String id);

    Result add(SysDictDataDto dto);

    Result delete(String id);

    Result updateOne(SysDictDataDto dto);
}

