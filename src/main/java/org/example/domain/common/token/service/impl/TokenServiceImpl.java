package org.example.domain.common.token.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.example.consts.Constant;
import org.example.domain.common.token.service.TokenService;
import org.example.domain.sysuser.entity.SysUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenServiceImpl implements TokenService {

    @Value("${token.expires}")
    private Integer expires;

    @Override
    public String getToken(SysUser sysUser) {
        String token = "";
        token = JWT.create().withAudience(sysUser.getId(), sysUser.getUserId(), sysUser.getPassword())// 将 user id 保存到 token 里面
                .withClaim(Constant.TOKEN.TOKEN_USERID, sysUser.getUserId())
                .withClaim(Constant.TOKEN.TOKEN_PASSWORD, sysUser.getPassword())
                .withExpiresAt(new Date(System.currentTimeMillis() + expires * 60 * 1000))
                .sign(Algorithm.HMAC256(sysUser.getPassword()));// 以 password 作为 token 的密钥
        return token;
    }
}
