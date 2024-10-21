package org.example.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "whitelist") // 配置文件的前缀
@Data
public class WhiteListConfig {

    private List<String> interfaceList;

}