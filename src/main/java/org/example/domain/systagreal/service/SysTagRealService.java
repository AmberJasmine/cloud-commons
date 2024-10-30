package org.example.domain.systagreal.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.systagreal.entity.SysTagReal;
import org.example.until.Result;

import java.util.List;


public interface SysTagRealService extends IService<SysTagReal> {

    Result tagForData(String id, List<String> tagIds);
}

