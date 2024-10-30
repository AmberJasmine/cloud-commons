package org.example.domain.filecollect.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.filecollect.entity.FileCollect;
import org.example.until.Result;


public interface FileCollectService extends IService<FileCollect> {

    Result<String> collectFile(String id);
}

