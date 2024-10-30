package org.example.domain.filecollect.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.example.consts.Constant;
import org.example.domain.filecollect.entity.FileCollect;
import org.example.domain.filecollect.mapper.FileCollectMapper;
import org.example.domain.filecollect.service.FileCollectService;
import org.example.until.Result;
import org.example.until.UuidUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@Service("fileCollectService")
public class FileCollectServiceImpl
        extends ServiceImpl<FileCollectMapper, FileCollect>
        implements FileCollectService {

    @Autowired
    private FileCollectService fileCollectService;

    @Autowired
    private HttpSession httpSession;


    @Override
    public Result<String> collectFile(String id) {
        FileCollect exist = this.fileCollectService.getOne(new LambdaQueryWrapper<FileCollect>()
                .eq(FileCollect::getFileId, id)
                .eq(FileCollect::getUserId, String.valueOf(httpSession.getAttribute(Constant.SESSION.USER_ID))));
        if (Objects.isNull(exist)) {
            this.fileCollectService.getBaseMapper().insert(FileCollect.builder().id(UuidUtils.getUuid())
                    .fileId(id).userId(String.valueOf(httpSession.getAttribute(Constant.SESSION.USER_ID))).createTime(LocalDateTime.now())
                    .deleted(Constant.STATUS.STATUS_0).build());
        } else {
            this.fileCollectService.update(new LambdaUpdateWrapper<FileCollect>()
                    .eq(FileCollect::getId, exist.getId())
                    .set(FileCollect::getDeleted, Objects.equals("0", exist.getDeleted()) ? "1" : "0"));
        }
        return Objects.isNull(exist) || Objects.equals("1", exist.getDeleted())
                ? Result.ofSuccess("已收藏") : Result.ofSuccess("已取消收藏");
    }
}

