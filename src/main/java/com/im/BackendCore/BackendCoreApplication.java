package com.im.BackendCore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class BackendCoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendCoreApplication.class, args);
	}

}
