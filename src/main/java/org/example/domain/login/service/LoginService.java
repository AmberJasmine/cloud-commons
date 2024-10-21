package org.example.domain.login.service;

import org.example.domain.login.dto.LoginByPhoneDto;
import org.example.domain.login.dto.LoginByUserIdDto;
import org.example.domain.login.dto.LoginDto;
import org.example.until.Result;

public interface LoginService {

    Result login(LoginDto loginDto) throws Exception;

    Result loginByUserId(LoginByUserIdDto loginByUserIdDto) throws Exception;

    Result loginByPhone(LoginByPhoneDto loginByPhoneDto) throws Exception;

    Result<String> pictureOfUser();
}

