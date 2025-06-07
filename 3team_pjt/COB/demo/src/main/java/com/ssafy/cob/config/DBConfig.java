package com.ssafy.cob.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "com.ssafy.cob.domain.*.model.dao")
public class DBConfig {
	
}