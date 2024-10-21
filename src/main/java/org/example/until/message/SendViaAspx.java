package org.example.until.message;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.example.until.RandomUtil;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class SendViaAspx {
    private static CloseableHttpClient httpclient;

    public static void main(String[] args) throws Exception {
        String mobile = "19191709392";
        String message = "【神州】您的短信验证码为[" + RandomUtil.getFourBitRandom() + "]，一分钟内有效，信息安全，请勿泄露";
        String s = sentMessage(message, mobile, "2023-11-08 12:28:00");
        System.out.println("s = " + s);
    }

    public static String sentMessage(String message, String phone, String sendtime) throws Exception {
        httpclient = SSLClient.createSSLClientDefault();
        //接口地址
        String url = "https://dx.ipyy.net/smsJson.aspx";
        //用户ID
        String userid = "";
        //用户账号名
        String account = "OT00954";
        //接口密码
        String password = "y2c7k5pe";
        //多个手机号用逗号分隔
        String mobile = phone;
        String text = message;
        String sendTime = sendtime;
        //扩展号，没有请留空
        String extno = "";
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-type", "application/x-www-form-urlencoded");
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("action", "send"));
        nvps.add(new BasicNameValuePair("userid", userid));
        nvps.add(new BasicNameValuePair("account", account));
        nvps.add(new BasicNameValuePair("password", MD5.GetMD5Code(password)));
        nvps.add(new BasicNameValuePair("mobile", mobile));
        nvps.add(new BasicNameValuePair("content", text));
        nvps.add(new BasicNameValuePair("sendTime", sendTime));
        nvps.add(new BasicNameValuePair("extno", extno));
        post.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
        HttpResponse response = httpclient.execute(post);
        HttpEntity entity = response.getEntity();
        String returnString = EntityUtils.toString(entity);
        EntityUtils.consume(entity);
        log.info("发送短信响应信息 = \n{}", returnString);
        return returnString;
    }

}