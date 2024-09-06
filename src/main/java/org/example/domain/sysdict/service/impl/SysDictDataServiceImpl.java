package org.example.domain.sysdict.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.example.domain.sysdict.dao.SysDictDataDao;
import org.example.domain.sysdict.dto.SysDictDataDto;
import org.example.domain.sysdict.entity.SysDictData;
import org.example.domain.sysdict.service.SysDictDataService;
import org.example.domain.sysdict.vo.SysDictDataVo;
import org.example.consts.Constant;
import org.example.until.Result;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service("sysDictDataService")
public class SysDictDataServiceImpl
        extends ServiceImpl<SysDictDataDao, SysDictData>
        implements SysDictDataService {

    @Autowired
    private SysDictDataService sysDictDataService;

    @Autowired
    private HttpSession httpSession;

    @Override
    public Result<List<SysDictDataVo>> listByDictId(String dictId, boolean forbidden) {
        List<SysDictData> list = this.sysDictDataService.list(new LambdaQueryWrapper<SysDictData>()
                .eq(StringUtils.isNotBlank(dictId), SysDictData::getDictId, dictId)
                .eq(forbidden, SysDictData::getStatus, "0")
                .orderByAsc(SysDictData::getSort));
        ArrayList<SysDictDataVo> dictDataVos = new ArrayList<>();
        for (SysDictData sysDictData : list) {
            SysDictDataVo sysDictDataVo = new SysDictDataVo();
            BeanUtils.copyProperties(sysDictData, sysDictDataVo);
            dictDataVos.add(sysDictDataVo);
        }
        return Result.ofSuccess("", dictDataVos);
    }

    @Override
    public Result<String> able(String id) {
        SysDictData byId = this.sysDictDataService.getById(id);
        if (Objects.isNull(byId)) {
            return Result.ofFail("不存在该字典内容");
        }
        String status = byId.getStatus();
        byId.setStatus(Objects.equals(Constant.STATUS.STATUS_0, status) ?
                Constant.STATUS.STATUS_1 : Constant.STATUS.STATUS_0);
        byId.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        byId.setUpdateBy((String) this.httpSession.getAttribute(Constant.SESSION.USER_ID));
        this.sysDictDataService.updateById(byId);
        String message = Objects.equals(Constant.STATUS.STATUS_0, status) ? "已禁用" : "已启用";
        return Result.ofSuccess(message);
    }

    @Override
    public Result add(SysDictDataDto dto) {
        String dictId = dto.getDictId();
        String key = dto.getKey();
        String value = dto.getValue();
        // dictId字典是否存在名为key的字典
        List<SysDictData> list = this.sysDictDataService.list(new LambdaQueryWrapper<SysDictData>()
                .eq(SysDictData::getDictId, dictId)
                .eq(SysDictData::getKey, key)
        );
        if (CollectionUtils.isNotEmpty(list)) {
            return Result.ofFail("已经存在此字典项");
        }
        SysDictData sysDictData = new SysDictData();
        sysDictData.setDictId(dictId);
        sysDictData.setKey(key);
        sysDictData.setValue(value);
        sysDictData.setSort(dto.getSort());
        sysDictData.setRemark(dto.getRemark());
        sysDictData.setStatus(Constant.STATUS.STATUS_0);
//        sysDictData.setCreateBy((String) this.httpSession.getAttribute(Constant.SESSION.USER_ID));
        sysDictData.setCreateBy(dto.getCreateBy());
        sysDictData.setCreateTime(new Timestamp(System.currentTimeMillis()));
        this.sysDictDataService.save(sysDictData);
        return Result.ofSuccess("字典新增成功", sysDictData);
    }

    @Override
    public Result delete(String id) {
        boolean b = this.sysDictDataService.removeById(id);
        return b ? Result.ofSuccess("删除成功") : Result.ofFail("删除失败");
    }

    @Override
    public Result updateOne(SysDictDataDto dto) {
        String id = dto.getId();
        String key = dto.getKey();
        String keyOld = dto.getKeyOld();
        String dictId = dto.getDictId();
//        这个字典下是否存在这个key，key不做更改查询筛除oldKey
        List<SysDictData> list = this.sysDictDataService.list(new LambdaQueryWrapper<SysDictData>()
                .eq(SysDictData::getDictId, dictId)
                .eq(SysDictData::getKey, key));
        List<SysDictData> collect = list.stream().filter(p -> !Objects.equals(p.getKey(), keyOld)).collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(collect)) {
            return Result.ofFail("已经存在该字典项" + key);
        }
        this.sysDictDataService.update(new LambdaUpdateWrapper<SysDictData>()
                .eq(SysDictData::getId, id)
                .set(SysDictData::getKey, key)
                .set(SysDictData::getValue, dto.getValue())
                .set(SysDictData::getSort, dto.getSort())
                .set(SysDictData::getUpdateBy,dto.getUpdateBy())
                .set(SysDictData::getUpdateTime, LocalDateTime.now())
                .set(SysDictData::getRemark, dto.getRemark()));
        return Result.ofSuccess("更新成功");
    }

}

