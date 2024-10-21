package org.example.domain.sysuser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.example.consts.Constant;
import org.example.domain.common.token.service.TokenService;
import org.example.domain.sysuser.dao.SysUserDao;
import org.example.domain.sysuser.dto.ChangePassByPhoneDto;
import org.example.domain.sysuser.dto.SysUserChangePassDto;
import org.example.domain.sysuser.dto.SysUserDto;
import org.example.domain.sysuser.dto.SysUserSearchDto;
import org.example.domain.sysuser.entity.SysUser;
import org.example.domain.sysuser.service.SysUserService;
import org.example.domain.sysuser.vo.SysUserVo;
import org.example.until.DateUtils;
import org.example.until.Result;
import org.example.until.sign.MD5;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * (SysUser)表服务实现类
 *
 * @author makejava
 * @since 2022-07-29 08:06:30
 */
@Slf4j
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUser> implements SysUserService {

    @Value(value = "${md5-key}")
    public String md5Key;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private TokenService tokenService;

    @Override
    public Result save(SysUserDto sysUserDto) throws Exception {
        String userId = sysUserDto.getUserId();
        String phone = sysUserDto.getPhone();
        List<SysUser> list = this.sysUserService.list(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserId, userId)
                .eq(SysUser::getStatus, Constant.STATUS.STATUS_0));
        if (CollectionUtils.isEmpty(list)) {
            return Result.ofFail("已经存在该用户[" + userId + "]");
        }
        List<SysUser> list1 = this.sysUserService.list(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getPhone, phone)
                .eq(SysUser::getStatus, Constant.STATUS.STATUS_0));
        if (CollectionUtils.isNotEmpty(list1)) {
            return Result.ofFail("手机号码[" + phone + "]已经注册");
        }

        SysUser sysUser = new SysUser();
        BeanUtils.copyProperties(sysUserDto, sysUser);
        String password = MD5.md5(sysUserDto.getPd(), md5Key);
        sysUser.setPassword(password);
        sysUser.setType("1");
        sysUser.setStatus("0");
        sysUser.setCreateTime(DateUtils.getNowDate());
        //短信校验
        Object value = redisTemplate.opsForValue().get(phone);
        if (Objects.isNull(value)) {
            return Result.ofFail("无效验证码");
        }
        if (!Objects.equals(value, sysUserDto.getMessageCode())) {
            return Result.ofFail("短信验证码不正确");
        }

        int insert = this.sysUserService.getBaseMapper().insert(sysUser);
        return insert > 0 ? Result.ofSuccess("注册成功") : Result.ofFail("注册失败");
    }

    @Override
    public Result changePassword(SysUserChangePassDto dto) throws Exception {
        String userId = dto.getUserId();
        if (!Objects.equals(dto.getPd(), dto.getCheckPd())) {
            return Result.ofFail("两次输入的新密码不一致");
        }
        SysUser one = this.sysUserService.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserId, userId)
                .eq(SysUser::getStatus, Constant.STATUS.STATUS_0));
        log.info("用户={},{}", userId, one);
        if (Objects.isNull(one)) {
            return Result.ofFail("不存在该用户[" + userId + "]");
        }
        if (!Objects.equals(one.getPd(), dto.getOldPd())) {
            return Result.ofFail("旧密码错误");
        }
//        one.setPd(dto.getPd());
//        one.setPassword(MD5.md5(dto.getPd(), md5Key));
//        this.sysUserService.updateById(one);

        this.sysUserService.update(new LambdaUpdateWrapper<SysUser>()
                .eq(SysUser::getUserId, dto.getUserId())
                .eq(SysUser::getStatus, Constant.STATUS.STATUS_0)
                .set(SysUser::getUpdateUser, userId)
                .set(SysUser::getUpdateTime, DateUtils.getNowDate())
                .set(SysUser::getPd, dto.getPd().trim())
                .set(SysUser::getPassword, MD5.md5(dto.getPd().trim(), md5Key)));
        return Result.ofSuccess("密码修改成功");
    }

    @Override
    public Result changePdByPhone(ChangePassByPhoneDto dto) throws Exception {
        String phone = dto.getPhone();
        if (!Objects.equals(dto.getPd(), dto.getCheckPd())) {
            return Result.ofFail("两次输入的新密码不一致");
        }
        SysUser one = this.sysUserService.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getPhone, phone)
                .eq(SysUser::getStatus, Constant.STATUS.STATUS_0));
        log.info("用户={}", one);
        if (Objects.isNull(one)) {
            return Result.ofFail("该手机号码未注册[" + phone + "]");
        }
        one.setPd(dto.getPd());
        one.setPassword(MD5.md5(dto.getPd(), md5Key));
        one.setUpdateUser(one.getCreateUser());
        one.setUpdateTime(DateUtils.getNowDate());
        this.sysUserService.updateById(one);

        String token = this.tokenService.getToken(one);
        return Result.ofSuccess("密码修改成功", token);
    }

    @Override
    public Result pages(SysUserSearchDto dto) {
        Page<SysUser> page = this.sysUserService.page(new Page<SysUser>(dto.getPageCurrent(), dto.getPageSize()),
                new LambdaQueryWrapper<SysUser>()
                        .like(StringUtils.isNotBlank(dto.getId()), SysUser::getId, dto.getId())
                        .like(StringUtils.isNotBlank(dto.getUserId()), SysUser::getUserId, dto.getUserId())
                        .like(StringUtils.isNotBlank(dto.getPhone()), SysUser::getPhone, dto.getPhone())
                        .eq(StringUtils.isNotBlank(dto.getStatus()), SysUser::getStatus, dto.getStatus())
                        .eq(StringUtils.isNotBlank(dto.getType()), SysUser::getType, dto.getType())
                        .and(Objects.nonNull(dto.getStartTime()) || Objects.nonNull(dto.getEndTime()),
                                p -> p.ge(Objects.nonNull(dto.getStartTime()), SysUser::getCreateTime, dto.getStartTime())
                                        .le(Objects.nonNull(dto.getEndTime()), SysUser::getCreateTime, dto.getEndTime()))
                        .orderByDesc(SysUser::getCreateTime));
        Page<SysUserVo> pageinfo = new Page<>();
        BeanUtils.copyProperties(page, pageinfo);
        List<SysUser> records = page.getRecords();
        ArrayList<SysUserVo> sysUserVos = new ArrayList<>();
        for (SysUser recordT : records) {
            SysUserVo sysUserVo = new SysUserVo();
            BeanUtils.copyProperties(recordT, sysUserVo);
            sysUserVos.add(sysUserVo);
        }
        pageinfo.setRecords(sysUserVos);
        return Result.ofSuccess("", pageinfo);
    }

    @Override
    public Result userByUserId(String userId) {
        SysUser one = this.sysUserService.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserId, userId)
                .eq(SysUser::getStatus, Constant.STATUS.STATUS_0));
        SysUserVo sysUserVo = new SysUserVo();
        BeanUtils.copyProperties(one, sysUserVo);
        sysUserVo.setRealUserName(one.getUserName());
        return Result.ofSuccess("查询成功", sysUserVo);
    }

    @Override
    public Result updateByUserId(SysUserDto sysUserDto) {
        if (StringUtils.isBlank(sysUserDto.getPhoto())) {
            return Result.ofFail("图像不能为空");
        }
        if (StringUtils.isBlank(sysUserDto.getUserId())) {
            return Result.ofFail("用户编号不能为空");
        }
        this.sysUserService.update(new LambdaUpdateWrapper<SysUser>()
                .set(SysUser::getProfilePhoto, sysUserDto.getPhoto())
                .eq(SysUser::getStatus, Constant.STATUS.STATUS_0)
                .eq(SysUser::getUserId, sysUserDto.getUserId()));

        return Result.ofSuccess("头像更新成功");
    }

    @Override
    public SysUser userByUP(String userId, String password) throws Exception {
        SysUser one = this.sysUserService.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserId, userId)
                .eq(SysUser::getPassword, MD5.md5(password, md5Key))
                .eq(SysUser::getStatus, Constant.STATUS.STATUS_0));
        log.info("userByUP = {}", one);
        return one;
    }

    @Override
    public Result updateRealNameById(SysUserDto sysUserDto) {
        if (StringUtils.isBlank(sysUserDto.getRealUserName())) {
            return Result.ofFail("用户名不能为空");
        }
        if (StringUtils.isBlank(sysUserDto.getId())) {
            return Result.ofFail("id不能为空");
        }
        this.sysUserService.update(new LambdaUpdateWrapper<SysUser>()
                .set(SysUser::getUserName, sysUserDto.getRealUserName())
                .eq(SysUser::getStatus, Constant.STATUS.STATUS_0)
                .eq(SysUser::getId, sysUserDto.getId()));
        return Result.ofSuccess("用户名更新成功");
    }
}

