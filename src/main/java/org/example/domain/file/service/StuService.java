package org.example.domain.file.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.domain.file.IService.StuIService;
import org.example.domain.file.dto.StuDto;
import org.example.domain.file.dto.StuPageDto;
import org.example.domain.file.entity.StuEntity;
import org.example.domain.file.mapper.StuMapper;
import org.example.until.Result;
import org.example.until.UuidUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Create by Administrator
 * Data 15:23 2021/10/3 星期日
 */
@Slf4j
@Service
public class StuService extends ServiceImpl<StuMapper, StuEntity>
        implements StuIService {

    @Autowired
    private StuMapper stuMapper;

    @Autowired
    private StuIService stuIService;

    public List<StuEntity> getStus() {
        return stuMapper.selectList(null);
    }

    public List<StuEntity> selectAllAdd() {
        return stuMapper.selectAllAdd();
    }

    public List<StuEntity> getStus1(String name) {
        List<StuEntity> stuEntities = stuMapper.selectList(null);
        return stuEntities.stream()
                .filter(p -> p.getName().contains(name))
                .collect(Collectors.toList());
    }

    public List<StuEntity> getAll(List<String> idList) {
        return stuMapper.getAll(idList);
    }


    public Result<Integer> addOne(StuDto stuDto) {
        stuDto.setId(UuidUtils.getUuid());
        log.info("stuDto = {}", stuDto);
        StuEntity build = StuEntity.builder().build();
        BeanUtils.copyProperties(stuDto, build);
        build.setCreateTime(LocalDateTime.now());
        build.setUpdateTime(LocalDateTime.now());
        log.info("StuEntity = {}", build);
        int insert = stuMapper.insert(build);
        return Result.ofSuccess(insert);
    }

    public Result<Integer> deleteOne(String id) {
        int i = stuMapper.deleteById(id);
        return Result.ofSuccess("删除成功", i);
    }

    public Result<Page<StuEntity>> getStuPAge(StuPageDto stuPageDto) {
        Page<StuEntity> stuEntityPage = new Page<>(stuPageDto.getPageCurrent(), stuPageDto.getPageSize());
        LambdaQueryWrapper<StuEntity> stuEntityLambdaQueryWrapper = new LambdaQueryWrapper<StuEntity>()
                .like(StringUtils.isNotBlank(stuPageDto.getName()), StuEntity::getName, stuPageDto.getName())
//                .orderByDesc(StuEntity::getUpdateTime)
                .orderByDesc(StuEntity::getId);
        Page<StuEntity> page = stuMapper.selectPage(stuEntityPage, stuEntityLambdaQueryWrapper);
        return Result.ofSuccess(page);
    }

    public Result<String> deleteList(List<String> list) {
        boolean b = stuIService.removeByIds(list);
        return Result.ofSuccess(b ? "删除成功" : "删除失败");

    }

    public Result<String> updateOne(StuDto stuDto) throws UnknownHostException {
        StuEntity stu = StuEntity.builder().build();
        BeanUtils.copyProperties(stuDto, stu);
        stu.setUpdateBy(InetAddress.getLocalHost().toString());
        stu.setUpdateTime(LocalDateTime.now());
        log.info("stu = {}", stu);
        boolean b = stuIService.updateById(stu);
        return Result.ofSuccess(b ? "更新成功" : "更新失败");
    }


}
