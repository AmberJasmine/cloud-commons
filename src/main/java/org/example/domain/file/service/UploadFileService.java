package org.example.domain.file.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.example.consts.Constant;
import org.example.domain.file.IService.UploadFileIService;
import org.example.domain.file.dto.UploadFileDto;
import org.example.domain.file.dto.UploadFileOutDto;
import org.example.domain.file.entity.UploadFileEntity;
import org.example.domain.file.mapper.UploadFileMapper;
import org.example.domain.filecollect.entity.FileCollect;
import org.example.domain.filecollect.service.FileCollectService;
import org.example.domain.systagmanage.entity.SysTagManage;
import org.example.domain.systagmanage.service.SysTagManageService;
import org.example.domain.systagreal.entity.SysTagReal;
import org.example.domain.systagreal.service.SysTagRealService;
import org.example.until.Result;
import org.example.until.UuidUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Create by Administrator
 * Data 15:23 2021/10/3 星期日
 */
@Slf4j
@Service
public class UploadFileService
        extends ServiceImpl<UploadFileMapper, UploadFileEntity>
        implements UploadFileIService {

    @Autowired
    private UploadFileMapper uploadFileMapper;

    @Autowired
    private SysTagManageService sysTagManageService;

    @Autowired
    SysTagRealService sysTagRealService;

    @Autowired
    private UploadFileIService uploadFileIService;

    @Autowired
    private FileCollectService fileCollectService;

    @Autowired
    private HttpSession httpSession;

    public Integer addOne(UploadFileDto uploadFileDto) {
        UploadFileEntity uploadFileEntity = UploadFileEntity.builder()
                .id(UuidUtils.getUuid()).equip(uploadFileDto.getEquip())
                .directory(uploadFileDto.getDirectory())
                .previewUrl(uploadFileDto.getPreviewUrl())
                .realUrl(uploadFileDto.getRealUrl())
                .uuid(uploadFileDto.getUuid())
                .fileName(uploadFileDto.getFileName())
                .fileType(uploadFileDto.getFileType())
                .createTime(LocalDateTime.now())
                .createBy(uploadFileDto.getCreateBy())
                .build();
        return uploadFileMapper.insert(uploadFileEntity);
    }

    public Result<Page<UploadFileOutDto>> getList(String directory, Long pageCurrent, Long pageSize, List<String> tagIdList,
                                                  String searchName, String createBy) {

        // 根据标签id获取到对应的数据id（需要处理标签id为空的时候，得到关系表的数据id，应该为全部数据id）
        List<String> masterIdList = this.sysTagRealService.list(new LambdaQueryWrapper<SysTagReal>()
                .in(CollectionUtils.isNotEmpty(tagIdList), SysTagReal::getTagId, tagIdList))
                .stream().map(SysTagReal::getMasterId).collect(Collectors.toList());
        log.info("masterIdList = {}", masterIdList);

        masterIdList.add("");
        Page<UploadFileEntity> page = this.uploadFileMapper.selectPage(new Page<>(pageCurrent, pageSize),
                new LambdaQueryWrapper<UploadFileEntity>()
                        .eq(StringUtils.isNotBlank(directory), UploadFileEntity::getDirectory, directory)
                        .eq(UploadFileEntity::getDeleted, Constant.STATUS.STATUS_0)
                        .like(StringUtils.isNotBlank(createBy), UploadFileEntity::getCreateBy, createBy)
                        .like(StringUtils.isNotBlank(searchName), UploadFileEntity::getFileName, searchName)
                        .in(CollectionUtils.isNotEmpty(tagIdList), UploadFileEntity::getId, masterIdList)
                        .orderByDesc(UploadFileEntity::getCreateTime));

        Page<UploadFileOutDto> pageInfo = new Page<>();
        BeanUtils.copyProperties(page, pageInfo);
        log.info("page = {}", page);
        log.info("pageInfo = {}", pageInfo);

        List<UploadFileEntity> records = page.getRecords();

        //本页数据，获取标签信息
        List<String> masterIds = records.stream().map(UploadFileEntity::getId).collect(Collectors.toList());
        Map<String, List<SysTagReal>> map = this.sysTagRealService.list().stream()
                .filter(p -> {
                    return masterIds.contains(p.getMasterId());
                }).collect(Collectors.groupingBy(SysTagReal::getMasterId));
        Map<String, List<String>> masterId2TagIdsMap = new HashMap<>();
        Set<Map.Entry<String, List<SysTagReal>>> entries = map.entrySet();
        for (Map.Entry<String, List<SysTagReal>> entry : entries) {
            masterId2TagIdsMap.put(entry.getKey(), entry.getValue().stream()
                    .sorted((t1, t2) -> t2.getCreateTime().compareTo(t1.getCreateTime()))
                    .map(SysTagReal::getTagId).collect(Collectors.toList()));
        }

        Map<String, String> tagId2NameMap = this.sysTagManageService.list().stream()
                .collect(Collectors.toMap(SysTagManage::getId, SysTagManage::getTagName));

//        是否收藏
        List<String> collectListByUser = this.fileCollectService.list(
                new LambdaQueryWrapper<FileCollect>()
                        .eq(FileCollect::getUserId, String.valueOf(httpSession.getAttribute(Constant.SESSION.USER_ID)))
                        .eq(FileCollect::getDeleted, Constant.STATUS.STATUS_0))
                .stream().map(FileCollect::getFileId).collect(Collectors.toList());
        log.info("collectListByUser = {}", collectListByUser);

//        本条信息收藏量
        HashMap<String, Integer> fileCollectMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(records)) {
            Map<String, List<FileCollect>> collect = this.fileCollectService.list(
                    new LambdaQueryWrapper<FileCollect>().eq(FileCollect::getDeleted, Constant.STATUS.STATUS_0)
                            .in(FileCollect::getFileId, records.stream().map(UploadFileEntity::getId).collect(Collectors.toList()))
            ).stream().collect(Collectors.groupingBy(FileCollect::getFileId));
            log.info("collect = {}", collect);
            collect.forEach((k, v) -> {
                fileCollectMap.put(k, v.size());
            });
        }

        ArrayList<UploadFileOutDto> pageInfoDtos = new ArrayList<>();
        records.forEach(p -> {
            UploadFileOutDto uploadFileOutDto = new UploadFileOutDto();
            BeanUtils.copyProperties(p, uploadFileOutDto);
            uploadFileOutDto.setFileName(p.getFileName().substring(p.getFileName().indexOf("_") + 1));
            List<String> tagIds = masterId2TagIdsMap.get(p.getId());//获取tagId集合
            ArrayList<String> tagNameList = new ArrayList<>();
            if (Objects.nonNull(tagIds)) {
                tagIds.forEach(t -> {
                    tagNameList.add(Optional.ofNullable(tagId2NameMap.get(t)).orElse(t));
                });
            }
            uploadFileOutDto.setTagsList(tagNameList);
            uploadFileOutDto.setTagsIdList(Objects.isNull(tagIds) ? Lists.newArrayList() : tagIds);
//            处理是否收藏问题
            uploadFileOutDto.setIsCollect(collectListByUser.contains(p.getId()));
//            收藏量
            uploadFileOutDto.setCollectCount(Optional.ofNullable(fileCollectMap.get(p.getId())).orElse(0));
            pageInfoDtos.add(uploadFileOutDto);
        });
        pageInfo.setRecords(pageInfoDtos);
        return Result.ofSuccess(pageInfo);
    }

    public UploadFileEntity getOne(String id) {
        return this.uploadFileIService.getOne(new LambdaQueryWrapper<UploadFileEntity>().eq(UploadFileEntity::getId, id));
    }

    public Result<Void> deleteById(String id) {
        this.uploadFileIService.update(new LambdaUpdateWrapper<UploadFileEntity>().eq(UploadFileEntity::getId, id)
                .set(UploadFileEntity::getDeleted, "1"));
        return Result.ofSuccess();
    }

}
