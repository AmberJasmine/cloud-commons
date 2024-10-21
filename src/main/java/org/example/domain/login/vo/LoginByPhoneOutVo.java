package org.example.domain.login.vo;

import lombok.Data;

@Data
public class LoginByPhoneOutVo {

    private String userId;

    private String realUserName;

    private String token;

    private String profilePhoto;

    private String userAgent;
}
