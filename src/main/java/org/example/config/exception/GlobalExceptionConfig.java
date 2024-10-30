package org.example.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

/**
 * 全局异常配置
 * Create by Administrator
 * Data 1:40 2021/12/26 星期日
 */
@Slf4j
public class GlobalExceptionConfig {
    @Bean
    public GlobalExceptionHandler globalExceptionHandler() {
        log.info("***global exception has been enable***");
        return new GlobalExceptionHandler();
    }
}
