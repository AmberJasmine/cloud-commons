package org.example.domain.login.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.example.pags.config.constant.Constant;
//import com.example.pags.mysql.demo.common.token.service.TokenService;
//import com.example.pags.mysql.demo.sys.login.controller.LoginController;
//import com.example.pags.mysql.demo.sys.login.dto.LoginByPhoneDto;
//import com.example.pags.mysql.demo.sys.login.dto.LoginByUserIdDto;
//import com.example.pags.mysql.demo.sys.login.dto.LoginDto;
//import com.example.pags.mysql.demo.sys.login.service.LoginService;
//import com.example.pags.mysql.demo.sys.login.vo.LoginByPhoneOutVo;
//import com.example.pags.mysql.demo.sys.login.vo.LoginByUserIdOutVo;
//import com.example.pags.mysql.demo.sys.sysuser.entity.SysUser;
//import com.example.pags.mysql.demo.sys.sysuser.service.SysUserService;
//import com.example.pags.until.Result;
//import com.example.pags.until.sign.MD5;
//import com.github.sarxos.webcam.Webcam;
//import com.github.sarxos.webcam.WebcamPanel;
//import com.github.sarxos.webcam.WebcamResolution;
//import com.github.sarxos.webcam.WebcamUtils;
//import com.github.sarxos.webcam.util.ImageUtils;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.github.sarxos.webcam.WebcamUtils;
import com.github.sarxos.webcam.util.ImageUtils;
import lombok.extern.slf4j.Slf4j;
import org.example.consts.Constant;
import org.example.domain.common.token.service.TokenService;
import org.example.domain.login.controller.LoginController;
import org.example.domain.login.dto.LoginByPhoneDto;
import org.example.domain.login.dto.LoginByUserIdDto;
import org.example.domain.login.dto.LoginDto;
import org.example.domain.login.service.LoginService;
import org.example.domain.login.vo.LoginByPhoneOutVo;
import org.example.domain.login.vo.LoginByUserIdOutVo;
import org.example.domain.sysuser.entity.SysUser;
import org.example.domain.sysuser.service.SysUserService;
import org.example.until.Result;
import org.example.until.sign.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Objects;

@Slf4j
@Service("loginService")
public class LoginServiceImpl implements LoginService {

    @Value(value = "${md5-key}")
    public String md5Key;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private TokenService tokenService;

    @Override
    public Result login(LoginDto loginDto) throws Exception {
        // 用户名密码
        SysUser one = this.sysUserService.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserId, loginDto.getUserId())
                .eq(SysUser::getPassword, MD5.md5(loginDto.getPd(), md5Key))
                .eq(SysUser::getStatus, Constant.STATUS.STATUS_0));
        if (Objects.isNull(one)) {
            return Result.ofFail("用户名密码不匹配");
        }
        // 图形验证码
        String code = (String) this.httpSession.getAttribute("pcode");
        code = redisTemplate.opsForValue().get(LoginController.FIX + "picture-code");
        log.info("picture-code = {}", code);
        if (!Objects.equals(code, loginDto.getPictureCode().trim().toLowerCase())) {
            return Result.ofFail("图形验证码有误");
        }
        // 短信验证码
        Object value = redisTemplate.opsForValue().get(loginDto.getPhone());
        if (!Objects.equals(value, loginDto.getMessageCode())) {
            return Result.ofFail("无效短信验证码");
        }
        this.httpSession.setAttribute(Constant.SESSION.USER_ID, loginDto.getUserId());
        String token = this.tokenService.getToken(one);
        log.info("登录（用户，手机） = {}", token);
        return Result.ofSuccess("登录成功", token);
    }

    @Override
    public Result loginByUserId(LoginByUserIdDto loginByUserIdDto) throws Exception {
        // 用户名密码
        SysUser one = this.sysUserService.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserId, loginByUserIdDto.getUserId())
                .eq(SysUser::getPassword, MD5.md5(loginByUserIdDto.getPd(), md5Key))
                .eq(SysUser::getStatus, Constant.STATUS.STATUS_0));
        if (Objects.isNull(one)) {
            return Result.ofFail("用户名密码不匹配");
        }
        // 图形验证码
        String code = (String) this.httpSession.getAttribute("pcode");
        log.info("想测试一下图形验证码存到session可以取到吗={}", code);
        code = redisTemplate.opsForValue().get(LoginController.FIX + "picture-code");
        log.info("picture-code = {}", code);
        if (!Objects.equals(code, loginByUserIdDto.getPictureCode().trim().toLowerCase())) {
            return Result.ofFail("图形验证码有误");
        }
        this.httpSession.setAttribute(Constant.SESSION.USER_ID, loginByUserIdDto.getUserId());
        String token = this.tokenService.getToken(one);
        log.info("用户名密码登录token = {}", token);
        LoginByUserIdOutVo loginByUserIdOutVo = new LoginByUserIdOutVo();
        loginByUserIdOutVo.setUserId(loginByUserIdDto.getUserId());
        loginByUserIdOutVo.setToken(token);
        loginByUserIdOutVo.setProfilePhoto(one.getProfilePhoto());
        loginByUserIdOutVo.setUserAgent(getAgent());
        loginByUserIdOutVo.setRealUserName(one.getUserName());
        return Result.ofSuccess("登录成功", loginByUserIdOutVo);
    }

    private String getAgent() {
        String userAgent = this.request.getHeader("User-Agent");
        if (userAgent != null && (userAgent.contains("Windows") || userAgent.contains("Macintosh"))) {
            // PC端访问
            return "pc";
        } else if (userAgent != null && userAgent.contains("Mobile")) {
            // 移动端访问
            return "mobile";
        } else {
            // 其他情况
            return "unknown";
        }
    }

    @Override
    public Result loginByPhone(LoginByPhoneDto loginByPhoneDto) throws Exception {
        // 短信验证码
        Object value = redisTemplate.opsForValue().get(loginByPhoneDto.getPhone());
        if (!Objects.equals(value, loginByPhoneDto.getMessageCode())) {
            return Result.ofFail("无效短信验证码");
        }
        SysUser one = this.sysUserService.getOne(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getPhone, loginByPhoneDto.getPhone().trim())
                .eq(SysUser::getStatus, Constant.STATUS.STATUS_0));
        if (Objects.isNull(one)) {
            return Result.ofFail("该手机号[" + loginByPhoneDto.getPhone().trim() + "]未注册");
        }
        this.httpSession.setAttribute(Constant.SESSION.USER_ID, one.getUserId());
        String token = this.tokenService.getToken(one);
        log.info("短信登录，token={}", token);
        LoginByPhoneOutVo loginByPhoneOutVo = new LoginByPhoneOutVo();
        loginByPhoneOutVo.setUserId(one.getUserId());
        loginByPhoneOutVo.setToken(token);
        loginByPhoneOutVo.setProfilePhoto(one.getProfilePhoto());
        loginByPhoneOutVo.setUserAgent(getAgent());
        loginByPhoneOutVo.setRealUserName(one.getUserName());
        return Result.ofSuccess(loginByPhoneOutVo);
    }

    @Override
    public Result pictureOfUser() {
        Webcam webcam = Webcam.getDefault();
        webcam.setViewSize(WebcamResolution.VGA.getSize());
        WebcamPanel panel = new WebcamPanel(webcam);
        panel.setFPSDisplayed(true);
        panel.setDisplayDebugInfo(true);
        panel.setImageSizeDisplayed(true);
        panel.setMirrored(true);
        JFrame window = new JFrame("图像抓取");
        window.add(panel);
        window.setResizable(true);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.pack();
        window.setVisible(true);
        final JButton button = new JButton("点击进行拍照");
        window.add(panel, BorderLayout.CENTER);
        window.add(button, BorderLayout.SOUTH);
        window.setResizable(true);
        window.pack();
        window.setVisible(true);
        final File[] file = {null};
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                button.setEnabled(false);  //设置按钮不可点击
                //实现拍照保存-------start
                String fileName = "D://" + System.currentTimeMillis();       //保存路径即图片名称（不用加后缀）**
                WebcamUtils.capture(webcam, fileName, ImageUtils.FORMAT_PNG);
                file[0] = new File(fileName + "." + ImageUtils.FORMAT_PNG);
                log.info("file = {}", file[0]);
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        JOptionPane.showMessageDialog(null, "拍照成功");
                        button.setEnabled(true);    //设置按钮可点击
                        return;
                    }
                });
                //实现拍照保存-------end
            }
        });
        return Result.ofSuccess("", file[0]);
    }
}

