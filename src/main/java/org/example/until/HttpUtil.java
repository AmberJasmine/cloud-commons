package org.example.until;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.example.until.code.Resp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.ServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alex
 * @description:
 * @date 2022-03-09  14:38
 */
@Slf4j
//@Component
public class HttpUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    @Autowired
    private RestTemplate restTemplate;

    public Resp getResp(String url) {
        try {
            final ResponseEntity<Resp> forEntity = restTemplate.getForEntity(url, Resp.class);
            if (forEntity.getStatusCode().is2xxSuccessful()) {
                return forEntity.getBody();
            }
        } catch (RestClientException exception) {
            logger.error("request " + url + " error!");
        }
        return Resp.fail();

    }

    public static void main(String[] args) {
        Map map = new HashMap();
        map.put("id", "6232e6fa6eab7809883d6c24");
        HttpUtil httpUtil = new HttpUtil();
//        Resp resp = httpUtil.getResp("http://127.0.0.1:8082" + VssConstant.SHOW_IMAGE_URL);
//        Resp resp = httpUtil.getResp("http://121.5.179.95:8082/demo/getAllAdd");
//        System.out.println(resp.toString());

        String gbk = sendGet("http://121.5.179.95:8082/demo/getAllAdd", "json=true", "utf-8");
        log.info("gbk==={}",gbk);
        System.out.println("gbk = " + gbk);

        String gbk1 = sendGet("https://qifu-api.baidubce.com/ip/geo/v1/district", "json=true&ip=36.45.227.116", "utf-8");
        log.info("gbk1==={}",gbk1);
        System.out.println("gbk1 = " + gbk1);
    }



    public static String sendGet(String url, String param, String contentType) {
        StringBuilder result = new StringBuilder();
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            log.info("sendGet - {}", urlNameString);
            URL realUrl = new URL(urlNameString);
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.connect();
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), contentType));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            log.info("recv - {}", result);
        } catch (ConnectException e) {
            log.error("调用HttpUtils.sendGet ConnectException, url=" + url + ",param=" + param, e);
        } catch (SocketTimeoutException e) {
            log.error("调用HttpUtils.sendGet SocketTimeoutException, url=" + url + ",param=" + param, e);
        } catch (IOException e) {
            log.error("调用HttpUtils.sendGet IOException, url=" + url + ",param=" + param, e);
        } catch (Exception e) {
            log.error("调用HttpsUtil.sendGet Exception, url=" + url + ",param=" + param, e);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception ex) {
                log.error("调用in.close Exception, url=" + url + ",param=" + param, ex);
            }
        }
        return result.toString();
    }


    public static String getBodyString(ServletRequest request) {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = null;
        try (InputStream inputStream = request.getInputStream()) {
            reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            log.warn("getBodyString出现问题！");
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error(ExceptionUtils.getMessage(e));
                }
            }
        }
        return sb.toString();
    }
}
