package com.point.chat.pointadmin.config;

import com.point.chat.pointcommon.config.ChatConfig;
import com.point.chat.pointcommon.config.UploadFileConfig;
import com.point.chat.pointcommon.entity.OSSConfig;
import com.point.chat.pointcommon.utils.RedisUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.client.RestTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 公共Bean配置类
 */
@Configuration
public class CommBeanConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    @ConfigurationProperties(prefix = "aliyun.oss")
    public OSSConfig ossConfig() {
        return new OSSConfig();
    }

    @Bean
    @ConfigurationProperties(prefix = "chat")
    public ChatConfig getChatConfig() {
        return new ChatConfig();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 图片上传配置 bean
     */
    @Bean("imageConfig")
    @ConfigurationProperties(prefix = "mat.upload.image")
    public UploadFileConfig getImageConfig() {
        return new UploadFileConfig();
    }

    /**
     * 视频上传配置 bean
     */
    @Bean("videoConfig")
    @ConfigurationProperties(prefix = "mat.upload.video")
    public UploadFileConfig getVideoConfig() {
        return new UploadFileConfig();
    }

    /**
     * 文件上传配置 bean
     */
    @Bean("fileConfig")
    @ConfigurationProperties(prefix = "mat.upload.file")
    public UploadFileConfig getFileConfig() {
        return new UploadFileConfig();
    }

    @Bean(name = "redisUtil")
    public RedisUtil redisUtil(StringRedisTemplate redisTemplate) {
        return new RedisUtil(redisTemplate);
    }

/*    @Bean(name = "prodRedisUtil")
    @ConditionalOnProperty(name = "spring.profiles.active", havingValue = "dev")
    public RedisUtil prodRedisUtil() {
        return new RedisUtil(new RedisTemplateProdConfig().getTemplate());
    }*/

    /**
     * 添加分页插件
     */
    /*@Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));// 如果配置多个插件,切记分页最后添加
        // interceptor.addInnerInterceptor(new PaginationInnerInterceptor()); 如果有多数据源可以不配具体类型 否则都建议配上具体的DbType
        return interceptor;
    }*/
}

