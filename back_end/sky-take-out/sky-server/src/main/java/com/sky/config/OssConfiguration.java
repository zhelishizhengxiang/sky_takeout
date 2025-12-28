package com.sky.config;

import com.sky.properties.AliOssProperties;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @version 1.0
 * @ProjectName: sky-take-out
 * @Package: com.sky.config
 * @Description:
 * @Author: Simon
 * @CreateDate: 2025/12/29
 */
@Configuration
@Slf4j
public class OssConfiguration {

    /**
     * 注入阿里云文件上传工具类
     * @param aliOssProperties
     * @return
     * */
    @Bean
    @ConditionalOnMissingBean(AliOssUtil.class)
    public AliOssUtil aliOssUtil(AliOssProperties aliOssProperties) {
        log.info("开始创建阿里云文件上传工具类对象：{}", aliOssProperties);
        return new AliOssUtil(aliOssProperties.getEndpoint(),
                aliOssProperties.getAccessKeyId(),
                aliOssProperties.getAccessKeySecret(),
                aliOssProperties.getBucketName(),
                aliOssProperties.getRegion(),
                aliOssProperties.getProjectPath());
    }
}


