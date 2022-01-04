package com.mzj.mohome;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;


//开启mapper接口扫描，指定扫描基础包
@MapperScan(basePackages = "com.mzj.mohome.mapper")
@SpringBootApplication
@EnableCaching
@Configuration
@EnableScheduling
public class MohomeApplication {
    public static void main(String[] args) {
        SpringApplication.run(MohomeApplication.class, args);
    }
}



