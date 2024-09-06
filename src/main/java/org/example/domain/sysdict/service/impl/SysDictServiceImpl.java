package org.example.domain.sysdict.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.StringUtils;
import org.example.domain.sysdict.dao.SysDictDao;
import org.example.domain.sysdict.dto.SysDictDto;
import org.example.domain.sysdict.entity.SysDict;
import org.example.domain.sysdict.entity.SysDictData;
import org.example.domain.sysdict.service.SysDictDataService;
import org.example.domain.sysdict.service.SysDictService;
import org.example.domain.sysdict.vo.SysDictVo;
import org.example.consts.Constant;
import org.example.until.DateUtils;
import org.example.until.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service("sysDictService")
public class SysDictServiceImpl
        extends ServiceImpl<SysDictDao, SysDict>
        implements SysDictService {

    @Autowired
    private SysDictService sysDictService;

    @Autowired
    private SysDictDataService sysDictDataService;

    @Autowired
    private HttpSession httpSession;

    @Override
    public Result<Page<SysDictVo>> getPage(SysDictDto dto) {
        Page<SysDict> dictPage = new Page<>(dto.getPageCurrent(), dto.getPageSize());
        LambdaQueryWrapper<SysDict> wrapper = new LambdaQueryWrapper<SysDict>()
                .like(StringUtils.isNotBlank(dto.getDictId()), SysDict::getDictId, dto.getDictId())
                .like(StringUtils.isNotBlank(dto.getDictName()), SysDict::getDictName, dto.getDictName())
                .like(StringUtils.isNotBlank(dto.getRemark()), SysDict::getRemark, dto.getRemark())
                .like(StringUtils.isNotBlank(dto.getCreateBy()), SysDict::getCreateBy, dto.getCreateBy())
                .like(StringUtils.isNotBlank(dto.getUpdateBy()), SysDict::getUpdateBy, dto.getUpdateBy());
        if (Objects.nonNull(dto.getStartDate()) && Objects.nonNull(dto.getEndDate())) {
            wrapper.and(p -> p.ge(SysDict::getCreateTime, dto.getStartDate())
                    .le(SysDict::getCreateTime, dto.getEndDate()));
        }
        wrapper.orderByDesc(SysDict::getCreateTime);
        Page<SysDict> page = this.sysDictService.page(dictPage, wrapper);

        Page<SysDictVo> pageInfo = new Page<>();
        BeanUtils.copyProperties(page, pageInfo);
        ArrayList<SysDictVo> sysDictVos = new ArrayList<>();
        List<SysDict> records = page.getRecords();
        for (SysDict recordT : records) {
            SysDictVo sysDictVo = new SysDictVo();
            BeanUtils.copyProperties(recordT, sysDictVo);
            sysDictVos.add(sysDictVo);
        }
        pageInfo.setRecords(sysDictVos);
        return Result.ofSuccess("数据字典查询成功", pageInfo);
    }

    @Override
    public Result<SysDictVo> add(SysDictDto dto) {
        SysDict sysDict = new SysDict();
        String dictId = dto.getDictId();
        if (StringUtils.isBlank(dictId)) {
            return Result.ofFail("字典类别不能为空");
        }
        SysDict one = this.sysDictService.getOne(new LambdaQueryWrapper<SysDict>()
                .eq(SysDict::getDictId, dictId));
        if (Objects.nonNull(one)) {
            log.info("已经存在该字典，字典类别：{}，字典名称：{}", one.getDictId(), one.getDictName());
            return Result.ofFail("已经存在该字典[" + dictId + "]");
        }
        sysDict.setDictId(dictId);
        sysDict.setDictName(dto.getDictName());
        sysDict.setRemark(dto.getRemark());
        sysDict.setStatus(Constant.STATUS.STATUS_0);
        sysDict.setCreateBy((String) this.httpSession.getAttribute(Constant.SESSION.USER_ID));
        sysDict.setCreateTime(new Timestamp(System.currentTimeMillis()));
        this.sysDictService.save(sysDict);
        SysDictVo sysDictVo = new SysDictVo();
        BeanUtils.copyProperties(sysDict, sysDictVo);
        return Result.ofSuccess("字典新增成功", sysDictVo);
    }

    @Override
    public Result<SysDictVo> detail(SysDictDto dto) {
        SysDict byId = this.sysDictService.getById(dto.getId());
        SysDictVo sysDictVo = new SysDictVo();
        BeanUtils.copyProperties(byId, sysDictVo);
        return Result.ofSuccess("字典详情", sysDictVo);
    }

    @Override
    public Result<String> delete(SysDictDto dto) {
        boolean b = this.sysDictService.removeById(dto.getId());
        log.info("delete-b = {}", b);
        return b ? Result.ofSuccess("删除成功") : Result.ofFail("删除失败");
    }

    @Override
    public Result<SysDictVo> updateOne(SysDictDto dto) {
        String id = dto.getId();
        if (StringUtils.isBlank(id)) {
            return Result.ofFail("id不能为空");
        }
        String dictId = dto.getDictId();
        String dictIdOld = dto.getDictIdOld();
        if (StringUtils.isBlank(dictId)) {
            return Result.ofFail("字典类别不能为空");
        }

        List<SysDict> list = this.sysDictService.list(new LambdaQueryWrapper<SysDict>()
                .eq(SysDict::getDictId, dictId));
        // 去除原来的dictId
        List<SysDict> collect = list.stream()
                .filter(p -> !Objects.equals(p.getDictId(), dictIdOld)).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(collect)) {
            return Result.ofFail("已经存在名为" + dictId + "的字典");
        }
        // dictId没有更新,已更新
        this.sysDictService.update(new LambdaUpdateWrapper<SysDict>()
                .eq(SysDict::getId, id)
                .set(SysDict::getDictId, dictId)
                .set(SysDict::getDictName, dto.getDictName())
                .set(SysDict::getRemark, dto.getRemark())
                .set(SysDict::getUpdateBy, dto.getUpdateBy())
                .set(SysDict::getUpdateTime, DateUtils.getNowDate()));
        // dictId已经更新
        if (!Objects.equals(dictId, dictIdOld)) {
            this.sysDictDataService.update(new LambdaUpdateWrapper<SysDictData>()
                    .eq(SysDictData::getDictId, dictIdOld)
                    .set(SysDictData::getDictId, dictId)
                    .set(SysDictData::getUpdateBy, dto.getUpdateBy())
                    .set(SysDictData::getUpdateTime, DateUtils.getNowDate()));
        }

        return Result.ofSuccess("更新成功");
    }
}

