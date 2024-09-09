package org.example.until;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 时间工具类
 *
 * @author ruoyi
 */
@Slf4j
@Component
public class LogUtils {

    @Value(value = "${server.port}")
    private String port;

    @Value(value = "${spring.application.name}")
    private String springName;

    @Value(value = "${eureka.instance.hostname}")
    private String hostname;

    private static Map<String, String> map = new HashMap<>();

    static {
        map.put("127.0.0.1", "localHost");
        map.put("121.5.179.95", "腾讯");
        map.put("123.249.43.78", "华为");
        log.info("主机信息 = {}", map);
    }

    public String log() {
        return map.get(hostname) + "主机," + springName + ":" + port;
    }

}
