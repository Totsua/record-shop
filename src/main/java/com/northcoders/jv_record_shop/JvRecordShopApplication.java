package com.northcoders.jv_record_shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class JvRecordShopApplication {

	public static void main(String[] args) {
		SpringApplication.run(JvRecordShopApplication.class, args);
	}

}
