package com.xiao.hospital.hosp.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.xiao.hospital.hosp.mapper")
public class HospConfig {
}
