package org.example.until.message;


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import java.io.IOException;

public class Sendmessage {
    private static String Url = "https://dx.ipyy.net/sms.aspx";

    public static void main(String[] args) {
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(Url);
        String account = "OT00954";// 账户
        String password = "y2c7k5pe";// 密码
        String mobile = "19191709392";// 号码
        String content = "【神州】您的短信验证码为[9999]，一分钟内有效，信息安全，请勿泄露";// 短信内容  Your verification code is 5678[HuaXin]
        String result = encodeHexStr(8, content);
        client.getParams().setContentCharset("UTF-8");
        method.setRequestHeader("ContentType",
                "application/x-www-form-urlencoded;charset=UTF-8");
        NameValuePair[] data = {new NameValuePair("action", "send"),
                new NameValuePair("userid", ""),
                new NameValuePair("account", account),
                new NameValuePair("password", password),
                new NameValuePair("mobile", mobile),
                new NameValuePair("code", "8"),
                new NameValuePair("content", result),
                new NameValuePair("sendTime", ""),
                new NameValuePair("extno", ""),};
        method.setRequestBody(data);
        try {
            client.executeMethod(method);
            String Result = method.getResponseBodyAsString();
            System.out.println(Result);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // 字符编码成HEX
    public static String toHexString(String s) {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str.append(s4);
        }
        return "0x" + str;// 0x表示十六进制
    }

    // 转换十六进制编码为字符串
    public static String toStringHex(String s) {
        if ("0x".equals(s.substring(0, 2))) {
            s = s.substring(2);
        }
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(
                        s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            s = new String(baKeyword, "GBK");// UTF-16le:Not
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }

    /**
     * 把字节数组转换成16进制字符串
     *
     * @param bArray
     * @return
     */
    public static String bytesToHexString(byte[] bArray) {
        StringBuilder sb = new StringBuilder(bArray.length);
        String sTemp;
        for (byte b : bArray) {
            sTemp = Integer.toHexString(0xFF & b);
            if (sTemp.length() < 2)
                sb.append("0");
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    // 字符编码成HEX
    public static String encodeHexStr(int dataCoding, String realStr) {
        String strhex = "";
        try {
            byte[] bytSource = null;
            if (dataCoding == 15) {
                bytSource = realStr.getBytes("GBK");
            } else if (dataCoding == 3) {
                bytSource = realStr.getBytes("ISO-8859-1");
            } else if (dataCoding == 8) {
                bytSource = realStr.getBytes("UTF-16BE");
            } else {
                bytSource = realStr.getBytes("ASCII");
            }
            strhex = bytesToHexString(bytSource);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return strhex;
    }

    // hex编码还原成字符
    public static String decodeHexStr(int dataCoding, String hexStr) {
        String strReturn = "";
        try {
            int len = hexStr.length() / 2;
            byte[] bytSrc = new byte[len];
            for (int i = 0; i < len; i++) {
                String s = hexStr.substring(i * 2, 2);
                bytSrc[i] = Byte.parseByte(s, 512);
                Byte.parseByte(s, i);
                // bytSrc[i] = Byte.valueOf(s);
                // bytSrc[i] = Byte.Parse(s,
                // System.Globalization.NumberStyles.AllowHexSpecifier);
            }

            if (dataCoding == 15) {
                strReturn = new String(bytSrc, "GBK");
            } else if (dataCoding == 3) {
                strReturn = new String(bytSrc, "ISO-8859-1");
            } else if (dataCoding == 8) {
                strReturn = new String(bytSrc, "UTF-16BE");
                // strReturn = Encoding.BigEndianUnicode.GetString(bytSrc);
            } else {
                strReturn = new String(bytSrc, "ASCII");
                // strReturn =
                // System.Text.ASCIIEncoding.ASCII.GetString(bytSrc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strReturn;
    }
}
