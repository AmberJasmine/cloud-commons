package org.example.domain.file.IService;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import org.example.domain.file.dto.UploadFileRelQueryDto;
import org.example.domain.file.entity.UploadFileRelEntity;
import org.example.domain.file.vo.UploadFileEntityVo;
import org.example.until.Result;

public interface UploadFileRelIService extends IService<UploadFileRelEntity> {

    Page<UploadFileEntityVo> listByMaster(UploadFileRelQueryDto dto);

    PageInfo<UploadFileEntityVo> listByMasters(UploadFileRelQueryDto queryDto);

    Result<Void> del(UploadFileRelQueryDto queryDto);

}
