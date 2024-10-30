package org.example.domain.systagreal.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.collections4.CollectionUtils;
import org.example.domain.systagreal.dao.SysTagRealDao;
import org.example.domain.systagreal.entity.SysTagReal;
import org.example.domain.systagreal.service.SysTagRealService;
import org.example.until.Result;
import org.example.until.UuidUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 标签表(SysTagManage)表服务实现类
 *
 * @author makejava
 * @since 2022-08-14 12:42:01
 */
@Service("sysTagRealService")
public class SysTagRealServiceImpl extends ServiceImpl<SysTagRealDao, SysTagReal> implements SysTagRealService {

    @Autowired
    private SysTagRealService sysTagRealService;

    @Override
    public Result tagForData(String id, List<String> tagIds) {
        List<String> existIds = this.sysTagRealService.list(
                new LambdaQueryWrapper<SysTagReal>().eq(SysTagReal::getMasterId, id))
                .stream().map(SysTagReal::getTagId).collect(Collectors.toList());

        List<String> deleteTags = existIds.stream()
                .filter(p -> !tagIds.contains(p))
                .collect(Collectors.toList());


        if (CollectionUtils.isNotEmpty(deleteTags)) {
            this.sysTagRealService.remove(new LambdaQueryWrapper<SysTagReal>().eq(SysTagReal::getMasterId, id)
                    .in(CollectionUtils.isNotEmpty(deleteTags), SysTagReal::getTagId, deleteTags));
        }


        List<SysTagReal> sysTagReals = new ArrayList<>();
        for (String tagId : tagIds) {
            if (existIds.contains(tagId)) {
                continue;
            }
            SysTagReal sysTagReal = new SysTagReal();
            sysTagReal.setId(UuidUtils.getUuid());
            sysTagReal.setMasterId(id);
            sysTagReal.setTagId(tagId);
            sysTagReal.setCreateTime(LocalDateTime.now());
            sysTagReals.add(sysTagReal);
        }
        this.sysTagRealService.saveBatch(sysTagReals);
        return Result.ofSuccess();
    }
}

