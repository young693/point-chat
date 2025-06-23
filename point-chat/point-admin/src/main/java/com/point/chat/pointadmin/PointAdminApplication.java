package com.point.chat.pointadmin;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"com.point.chat.pointadmin", "com.point.chat.pointcommon"})
@MapperScan("com.point.chat.pointadmin.dao")
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class PointAdminApplication {

	public static void main(String[] args) {
		SpringApplication.run(PointAdminApplication.class, args);
	}

}
