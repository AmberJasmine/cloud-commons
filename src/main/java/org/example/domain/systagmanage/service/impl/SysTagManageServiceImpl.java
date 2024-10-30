package org.example.domain.systagmanage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.example.config.exception.base.CustomException;
import org.example.domain.systagmanage.dao.SysTagManageDao;
import org.example.domain.systagmanage.dto.SysTagManageDto;
import org.example.domain.systagmanage.entity.SysTagManage;
import org.example.domain.systagmanage.service.SysTagManageService;
import org.example.until.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 标签表(SysTagManage)表服务实现类
 *
 * @author makejava
 * @since 2022-08-14 12:42:01
 */
@Service("sysTagManageService")
public class SysTagManageServiceImpl
        extends ServiceImpl<SysTagManageDao, SysTagManage>
        implements SysTagManageService {

    @Autowired
    private SysTagManageDao sysTagManageDao;

    @Autowired
    private SysTagManageService sysTagManageService;

    @Override
    public Page<SysTagManage> page(SysTagManageDto dto) {
        LambdaQueryWrapper<SysTagManage> wapper = new LambdaQueryWrapper<>();
        SysTagManage sysTagManage = new SysTagManage();
        BeanUtils.copyProperties(dto, sysTagManage);
        return this.sysTagManageService.page(dto.getPage(), new QueryWrapper<>(sysTagManage));
    }

    @Override
    public List<SysTagManage> getList(SysTagManageDto dto) {
        SysTagManage sysTagManage = new SysTagManage();
        BeanUtils.copyProperties(dto, sysTagManage);
        return this.sysTagManageService.list(new LambdaQueryWrapper<>(sysTagManage).orderByDesc(SysTagManage::getCreateTime));
    }

    @Override
    public Result<SysTagManage> addOne(SysTagManage sysTagManage) {
        String tagName = sysTagManage.getTagName().trim();
        String tagType = sysTagManage.getTagType();
        String userId = sysTagManage.getUserId();
        if (StringUtils.isBlank(tagName)) {
            throw new CustomException("标签名称不能为空");
        }
        if (StringUtils.isBlank(tagType)) {
            throw new CustomException("标签类型不能为空");
        }
        if (StringUtils.isBlank(userId)) {
            throw new CustomException("当前操作人不能为空");
        }
        LambdaQueryWrapper<SysTagManage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysTagManage::getTagName, tagName);
        wrapper.eq(SysTagManage::getTagType, tagType);
        if ("keyWord".equals(tagType)) {
            List<SysTagManage> list = this.sysTagManageService.list(wrapper);
            if (!CollectionUtils.isEmpty(list)) {
                throw new CustomException("已经存在此关键词" + tagName);
            }
        }
        if ("tag".equals(tagType)) {
            wrapper.eq(SysTagManage::getUserId, userId);
            List<SysTagManage> list = this.sysTagManageService.list(wrapper);
            if (!CollectionUtils.isEmpty(list)) {
                throw new CustomException("当前用户已经存在此标签" + tagName);
            }
        }
        this.sysTagManageService.save(sysTagManage);
        return Result.ofSuccess("标签添加成功", sysTagManage);
    }

    @Override
    public Result<Void> deleteOne(SysTagManage sysTagManage) {
        if (StringUtils.isBlank(sysTagManage.getTagName())) {
            throw new CustomException("名称不能为空");
        }
        if (StringUtils.isBlank(sysTagManage.getTagType())) {
            throw new CustomException("类型不能为空");
        }
        if (StringUtils.isBlank(sysTagManage.getUserId())) {
            throw new CustomException("当前用户不能为空");
        }
        LambdaQueryWrapper<SysTagManage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysTagManage::getTagName, sysTagManage.getTagName())
                .eq(SysTagManage::getTagType, sysTagManage.getTagType())
                .eq(SysTagManage::getUserId, sysTagManage.getUserId());
        List<SysTagManage> list = this.sysTagManageService.list(wrapper);
        if (CollectionUtils.isEmpty(list)) {
            throw new CustomException("不存在此标签:" + sysTagManage.getTagName());
        }
        this.sysTagManageService.remove(wrapper);
        return Result.ofSuccess("删除成功");
    }
}

