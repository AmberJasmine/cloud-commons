package org.example.domain.login.controller;

import cn.hutool.core.text.StrBuilder;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.api.ApiController;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.domain.login.dto.ImageVerificationDto;
import org.example.domain.login.dto.LoginByPhoneDto;
import org.example.domain.login.dto.LoginByUserIdDto;
import org.example.domain.login.dto.LoginDto;
import org.example.domain.login.dto.MsgDto;
import org.example.domain.login.service.LoginService;
import org.example.until.HttpUtil;
import org.example.until.RandomUtil;
import org.example.until.Result;
import org.example.until.message.SendViaAspx;
import org.example.until.verification.ImageVerificationCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.TimeUnit;


@Slf4j
@RestController
@RequestMapping("login")
public class LoginController extends ApiController {

    public static String FIX = "";

    @Value(value = "${md5-key}")
    public String key;

    @Value(value = "${message.Uid}")
    private String Uid;

    @Value(value = "${message.Key}")
    public String Key;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private LoginService loginService;

    public static Map<String, Object> codeKey;

    @RequestMapping("/verifi-code")
    @ResponseBody
    @ApiOperation(value = "图形验证码（p）", notes = "图形验证码", httpMethod = "GET")
    public Result<ImageVerificationDto> getVerifiCode(Map<String, Object> requestMap, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ImageVerificationCode ivc = new ImageVerificationCode();     //使用验证码类，生成验证码类对象
        BufferedImage image = ivc.getBuffImg();                      //获取验证码
        request.getSession().setAttribute("text", ivc.getCode()); //将验证码的文本存在session中
//        ivc.write(response.getOutputStream());
        String base64 = ivc.getBase64();
        request.setCharacterEncoding("utf-8");
        String sessionVcode = (String) request.getSession().getAttribute("text");    //从session中获取真正的验证码
        httpSession.setAttribute("pcode", ivc.getCode());
        String pcode = (String) httpSession.getAttribute("pcode");
        System.out.println("pcode = " + pcode);

        String custIp = getIP(request);
        this.redisTemplate.delete(FIX + "picture-code");
        FIX = custIp + getFix();
        this.redisTemplate.opsForValue().set(FIX + "picture-code", pcode);
        ImageVerificationDto build = ImageVerificationDto.builder().base64(base64).code(sessionVcode).build();
        log.info("ImageVerificationDto = {}", build);
        return Result.ofSuccess(build);
    }

    @Autowired
    private SendViaAspx sendViaAspx;

    @ApiOperation(value = "发送短信验证码（p）", notes = "发送短信验证码", httpMethod = "GET")
    @GetMapping("/sendMessage")
    public Result sendMsm(@RequestParam String phone) throws Exception {
        //生成随机数
        String code = RandomUtil.getFourBitRandom();
//        log.info("smsText = {},smsMob = {}", code, phone);
//        HttpClientUtilMessage client = HttpClientUtilMessage.getInstance();
        //GBK发送
        String smsText = "【神州】您的短信验证码为【" + code + "】，一分钟内有效，信息安全，请勿泄露";
//        int resultGbk = client.sendMsgGbk(Uid, Key, smsText, phone);
//        if (resultGbk > 0) {
//            this.redisTemplate.opsForValue().set(phone, code, 1, TimeUnit.MINUTES);
//            log.info("验证码为 = {}", code);
//            log.info("GBK成功发送条数=={}", resultGbk);
//            return Result.ofSuccess(code);
//        } else {
//            log.info("短信发送失败，错误信息：{}", client.getErrorMsg(resultGbk));
//            return Result.ofFail(client.getErrorMsg(resultGbk));
//        }

        String s = this.sendViaAspx.sentMessage(smsText, phone, "");
        log.info("发送短信返回值 = {}", s);
        JSONObject formDataObj = JSONObject.parseObject(s);
        log.info("formDataObj = {}", formDataObj);
        String returnstatus = formDataObj.getString("returnstatus");
        String message = formDataObj.getString("message");
        if (Objects.equals("Success", returnstatus)) {
            this.redisTemplate.opsForValue().set(phone, code, 1, TimeUnit.MINUTES);
            return Result.ofSuccess(message, code);
        }
        return Result.ofFail(message);
    }

    @ApiOperation(value = "session", notes = "session", httpMethod = "POST")
    @PostMapping("/session")
    public Result session(HttpSession session) {
        String random = (String) session.getAttribute("text");
        String pcode = (String) httpSession.getAttribute("pcode");
        System.out.println("random = " + random);
        System.out.println("pcode = " + pcode);
        String id = httpSession.getId();
        String id1 = session.getId();
        log.info("sessionID = {},{}", id, id1);
        return Result.ofSuccess("", random + "," + pcode);
    }

    @ApiOperation(value = "登录", notes = "登录", httpMethod = "POST")
    @PostMapping("/login")
    public Result login(@RequestBody @Valid LoginDto loginDto) {
        String code = (String) httpSession.getAttribute("text");
        log.info("登录code = {}", code);
        Object pcode = httpSession.getAttribute("pcode");
        log.info("登录pcode = {}", pcode);
        try {
            return this.loginService.login(loginDto);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ofFail("异常", e.getMessage());
        }
    }


    @ApiOperation(value = "登录(userId)", notes = "登录(UserId)", httpMethod = "POST")
    @PostMapping("/loginByUserId")
    public Result loginByUserId(@RequestBody @Valid LoginByUserIdDto loginByUserIdDto) {
        String code = (String) httpSession.getAttribute("text");
        log.info("登录code = {}", code);
        Object pcode = httpSession.getAttribute("pcode");
        log.info("登录pcode = {}", pcode);
        try {
            return this.loginService.loginByUserId(loginByUserIdDto);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ofFail("异常", e.getMessage());
        }
    }

    @ApiOperation(value = "登录(phone)", notes = "登录(phone)", httpMethod = "POST")
    @PostMapping("/loginByPhone")
    public Result loginByPhone(@RequestBody @Valid LoginByPhoneDto loginByPhoneDto) {
        String code = (String) httpSession.getAttribute("text");
        log.info("登录code = {}", code);
        Object pcode = httpSession.getAttribute("pcode");
        log.info("登录pcode = {}", pcode);
        try {
            return this.loginService.loginByPhone(loginByPhoneDto);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ofFail(e.getMessage());
        }
    }

    @ApiOperation(value = "人像采集", notes = "人像采集", httpMethod = "POST")
    @PostMapping("/pictureOfUser")
    public Result pictureOfUser() {
        try {
            return this.loginService.pictureOfUser();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.ofFail(e.getMessage());
        }
    }

    @ApiOperation(value = "admin短信", notes = "admin短信", httpMethod = "POST")
    @PostMapping("/msg")
    public Result msg(@RequestBody MsgDto msgDto) throws Exception {
        String smsText = msgDto.getMessage();
        String smsMob = msgDto.getPhone();
        log.info("smsText = {},smsMob = {}", smsText, smsMob);
//        HttpClientUtilMessage client = HttpClientUtilMessage.getInstance();
//        //GBK发送
//        int resultGbk = client.sendMsgGbk(Uid, Key, smsText, smsMob);
//        if (resultGbk > 0) {
//            log.info("GBK成功发送条数=={}", resultGbk);
//            return Result.ofSuccess("发送成功");
//        } else {
//            log.info("短信发送失败，错误信息：{}", client.getErrorMsg(resultGbk));
//            return Result.ofFail(client.getErrorMsg(resultGbk));
//        }
        String respon = this.sendViaAspx.sentMessage(smsText, smsMob, "");
        log.info("发送短信返回值 = {}", respon);
        JSONObject formDataObj = JSONObject.parseObject(respon);
        log.info("formDataObj = {}", formDataObj);
        String returnstatus = formDataObj.getString("returnstatus");
        String message = formDataObj.getString("message");
        if (Objects.equals("Success", returnstatus)) {
            return Result.ofSuccess(message, smsText);
        }
        return Result.ofFail(message);
    }

    @ApiOperation(value = "客户端ip", notes = "客户端ip", httpMethod = "POST")
    @PostMapping("/custIp")
    public Result custIp(HttpServletRequest request) {
        String custIp = getIP(request);
        return Result.ofSuccess("客户端ip获取成功", custIp);
    }

    @ApiOperation(value = "网络地址", notes = "网络地址", httpMethod = "POST")
    @PostMapping("/ipAddress")
    public Result ipAddress(HttpServletRequest request) {
        String custIp = getIP(request);
        String ipAddress = HttpUtil.sendGet("https://qifu-api.baidubce.com/ip/geo/v1/district",
                "json=true&ip=" + custIp, "utf-8");
        log.info("custIp = {}", custIp);
        log.info("ipAddress = {}", ipAddress);
        if (Objects.equals(custIp, "127.0.0.1")) {
            return Result.ofSuccess("网络地址", "本地局域网");
        }
        JSONObject jsonObject = JSONObject.parseObject(ipAddress);
        String prov = jsonObject.getJSONObject("data").getString("prov");
        String city = jsonObject.getJSONObject("data").getString("city");
        String district = jsonObject.getJSONObject("data").getString("district");
        log.info("prov = {}", prov);
        return Result.ofSuccess("网络地址", (prov + " " + city + " " + district).trim());
    }

    @ApiOperation(value = "客户端ipV4", notes = "客户端ipV4", httpMethod = "POST")
    @PostMapping("/custIpV4")
    public Result custIpV4() {
        String custIp = getIPV4();
        return Result.ofSuccess("客户端ipV4获取成功", custIp);
    }

    @Autowired
    private static HttpServletRequest httpServletRequest;

    public static void main(String[] args) {
        String ipv4 = getIPV4();
        System.out.println("ipv4 = " + ipv4);
        String ip = getIP(httpServletRequest);
        System.out.println("ip = " + ip);
    }

    /**
     * 获取ipv4
     *
     * @return
     * @throws UnknownHostException
     */
    public static String getIPV4() {
        InetAddress inetAddress = null;
        try {
            inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return getFix();
    }

    /***
     * 获取客户端ip地址
     * @param request
     */
    public static String getIP(final HttpServletRequest request) {
        if (Objects.isNull(request)) {
            return getFix();
        }
        String ipStr = request.getHeader("x-forwarded-for");
        log.info("获取到的请求头 = {}",ipStr);
        if (StringUtils.isBlank(ipStr) || "unknown".equalsIgnoreCase(ipStr)) {
            ipStr = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipStr) || "unknown".equalsIgnoreCase(ipStr)) {
            ipStr = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipStr) || "unknown".equalsIgnoreCase(ipStr)) {
            ipStr = request.getRemoteAddr();
        }
        // 多个路由时，取第一个非unknown的ip
        final String[] arr = ipStr.split(",");
        for (final String str : arr) {
            if (!"unknown".equalsIgnoreCase(str)) {
                ipStr = str;
                break;
            }
        }
        //目的是将localhost访问对应的ip 0:0:0:0:0:0:0:1 转成 127.0.0.1。
        return ipStr.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ipStr;
    }

    public static String cust(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.contains(",")) {
            return ip.split(",")[0];
        } else {
            return ip;
        }
    }

    public static String getFix() {
        Random random = new Random();
        StrBuilder strBuilder = new StrBuilder();
        for (int i = 0; i < 16; i++) {
            strBuilder.append(random.nextInt(10));
        }
        return strBuilder.toString();
    }
}

