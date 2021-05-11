package com.invest.coin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients
@EnableAsync
@EnableScheduling
@SpringBootApplication
public class AutoCoinTraderApplication {

	public static void main(String[] args) {
		SpringApplication.run(AutoCoinTraderApplication.class, args);
	}

}
