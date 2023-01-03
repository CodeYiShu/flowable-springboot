package com.codeshu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "com.codeshu.dao")
public class FlowableSpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlowableSpringbootApplication.class, args);
	}

}
