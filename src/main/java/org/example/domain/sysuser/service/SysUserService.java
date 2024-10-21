package org.example.domain.sysuser.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.domain.sysuser.dto.ChangePassByPhoneDto;
import org.example.domain.sysuser.dto.SysUserChangePassDto;
import org.example.domain.sysuser.dto.SysUserDto;
import org.example.domain.sysuser.dto.SysUserSearchDto;
import org.example.domain.sysuser.entity.SysUser;
import org.example.until.Result;
import org.springframework.stereotype.Component;

/**
 * (SysUser)表服务接口
 *
 * @author makejava
 * @since 2022-07-29 08:06:29
 */
@Component
public interface SysUserService extends IService<SysUser> {

    Result save(SysUserDto sysUserDto) throws Exception;

    Result changePassword(SysUserChangePassDto dto) throws Exception;

    Result changePdByPhone(ChangePassByPhoneDto dto) throws Exception;

    Result pages(SysUserSearchDto sysUserSearchDto);

    Result userByUserId(String userId);

    Result updateByUserId(SysUserDto sysUserDto);

    SysUser userByUP(String userId, String password) throws Exception;

    Result updateRealNameById(SysUserDto sysUserDto);
}

