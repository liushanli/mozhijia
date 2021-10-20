package com.mzj.mohome;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;


//开启mapper接口扫描，指定扫描基础包
@MapperScan(basePackages = "com.mzj.mohome.mapper")
@SpringBootApplication
@EnableCaching
@Configuration
public class MohomeApplication {
    public static void main(String[] args) {
        SpringApplication.run(MohomeApplication.class, args);
    }


    /**
     * 文件上传配置
     * @return
     */
    /*@Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //文件最大
        factory.setMaxFileSize(DataSize.parse("1024000KB"));
        // 设置总上传数据总大小
        factory.setMaxRequestSize(DataSize.parse("1024000KB"));
        return factory.createMultipartConfig();
    }*/
}



