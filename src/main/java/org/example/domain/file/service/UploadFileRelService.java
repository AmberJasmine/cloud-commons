package org.example.domain.file.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import com.example.pags.config.constant.Constant;
//import com.example.pags.mysql.demo.start.IService.UploadFileRelIService;
//import com.example.pags.mysql.demo.start.dto.UploadFileRelQueryDto;
//import com.example.pags.mysql.demo.start.entity.UploadFileEntity;
//import com.example.pags.mysql.demo.start.entity.UploadFileRelEntity;
//import com.example.pags.mysql.demo.start.mapper.UploadFileRelMapper;
//import com.example.pags.mysql.demo.start.vo.UploadFileEntityVo;
//import com.example.pags.mysql.demo.sys.shop.entity.SysShopMenus;
//import com.example.pags.mysql.demo.sys.shop.service.SysShopMenusService;
//import com.example.pags.until.Result;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
//import org.jetbrains.annotations.NotNull;
import org.example.consts.Constant;
import org.example.domain.file.IService.UploadFileRelIService;
import org.example.domain.file.dto.UploadFileRelQueryDto;
import org.example.domain.file.entity.UploadFileEntity;
import org.example.domain.file.entity.UploadFileRelEntity;
import org.example.domain.file.mapper.UploadFileRelMapper;
import org.example.domain.file.vo.UploadFileEntityVo;
import org.example.domain.shop.entity.SysShopMenus;
import org.example.domain.shop.service.SysShopMenusService;
import org.example.until.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Create by Administrator
 * Data 15:23 2021/10/3 星期日
 */
@Slf4j
@Service
public class UploadFileRelService
        extends ServiceImpl<UploadFileRelMapper, UploadFileRelEntity>
        implements UploadFileRelIService {

    @Autowired
    private UploadFileRelMapper uploadFileRelMapper;

    @Autowired
    private UploadFileRelIService uploadFileRelIService;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private UploadFileService uploadFileService;

    @Autowired
    private SysShopMenusService sysShopMenusService;

    @Override
    public Page<UploadFileEntityVo> listByMaster(UploadFileRelQueryDto queryDto) {
        List<String> masterIds = queryDto.getMasterIds();
        Map<String, String> masterId2Name = this.sysShopMenusService.list(new LambdaQueryWrapper<SysShopMenus>().in(SysShopMenus::getId, masterIds))
                .stream()
                .collect(Collectors.toMap(SysShopMenus::getId, SysShopMenus::getName));
        log.info("文件挂载节点信息 = {}", masterId2Name);

        LambdaQueryWrapper<UploadFileRelEntity> wrapper = new LambdaQueryWrapper<UploadFileRelEntity>()
                .eq(UploadFileRelEntity::getBelongModel, queryDto.getBelongModel())
                .in(UploadFileRelEntity::getMasterId, masterIds)
                .eq(UploadFileRelEntity::getDeleted, "0");
        List<UploadFileRelEntity> list = this.uploadFileRelIService.list(wrapper);
        List<String> fileIds = list.stream().map(UploadFileRelEntity::getFileId).collect(Collectors.toList());
        log.info("关联的附件id集合 = {}", fileIds);
        if (CollectionUtils.isEmpty(list)) {
            return new Page<>();
        }
        // 附件编号与masterId的关系
        Map<String, String> fileId2MasterIdMap = list
                .stream()
                .collect(Collectors.toMap(UploadFileRelEntity::getFileId, UploadFileRelEntity::getMasterId));

        LambdaQueryWrapper<UploadFileEntity> wapper = new LambdaQueryWrapper<UploadFileEntity>().in(UploadFileEntity::getUuid, fileIds);
        String order = queryDto.getOrder();
        String prop = queryDto.getProp();
//        masterName,ascending masterName,descending
//        createTime,ascending createTime,descending
        if(Objects.isNull(order)){
            wapper.orderByDesc(UploadFileEntity::getCreateTime);
        }
        if(Objects.equals("ascending",order) && Objects.equals("createTime",prop)){
            wapper.orderByAsc(UploadFileEntity::getCreateTime);
        }
        if(Objects.equals("descending",order) && Objects.equals("createTime",prop)){
            wapper.orderByDesc(UploadFileEntity::getCreateTime);
        }
//        按照节点排序，无sql实现困难
        Page<UploadFileEntity> page = this.uploadFileService.getBaseMapper()
                .selectPage(new Page<>(queryDto.getPageCurrent(), queryDto.getPageSize()), wapper);
        Page<UploadFileEntityVo> outPage = new Page<>();
        BeanUtils.copyProperties(page, outPage);
        List<UploadFileEntityVo> uploadFileEntityVos = getUploadFileEntityVos(page, fileId2MasterIdMap, masterId2Name);
        outPage.setRecords(uploadFileEntityVos);

        return outPage;
    }

    @Override
    public PageInfo<UploadFileEntityVo> listByMasters(UploadFileRelQueryDto queryDto) {
        PageHelper.startPage(Integer.parseInt(queryDto.getPageCurrent().toString()),Integer.parseInt(queryDto.getPageSize().toString()));
        List<UploadFileEntityVo> uploadFileEntityVos = this.uploadFileRelMapper.pageByMasterId(queryDto);
        return new PageInfo<>(uploadFileEntityVos);
    }

    @Override
    public Result<Void> del(UploadFileRelQueryDto queryDto) {
        if(StringUtils.isBlank(queryDto.getId())){
            return Result.ofFail("id不能为空");
        }
        this.uploadFileRelIService.update(new LambdaUpdateWrapper<UploadFileRelEntity>()
                .set(UploadFileRelEntity::getDeleted, Constant.STATUS.STATUS_1)
                .eq(UploadFileRelEntity::getId, queryDto.getId()));
        return Result.ofSuccess("删除成功");
    }

    @NotNull
    private static List<UploadFileEntityVo> getUploadFileEntityVos(
            Page<UploadFileEntity> page, Map<String, String> fileId2MasterIdMap, Map<String, String> masterId2Name) {
        List<UploadFileEntityVo> uploadFileEntityVos = new ArrayList<>();
        List<UploadFileEntity> records = page.getRecords();

        records.forEach(p -> {
            UploadFileEntityVo uploadFileEntityVo = new UploadFileEntityVo();
            BeanUtils.copyProperties(p, uploadFileEntityVo);
            uploadFileEntityVo.setFileName(p.getFileName().substring(37));
            uploadFileEntityVo.setMasterId(fileId2MasterIdMap.get(p.getUuid()));
            uploadFileEntityVo.setMasterName(masterId2Name.get(fileId2MasterIdMap.get(p.getUuid())));
            uploadFileEntityVos.add(uploadFileEntityVo);
        });
        return uploadFileEntityVos;
    }

}
