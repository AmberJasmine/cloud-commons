package org.example.domain.common.token.service;


import org.example.domain.sysuser.entity.SysUser;

public interface TokenService {
    String getToken(SysUser sysUser);
}
