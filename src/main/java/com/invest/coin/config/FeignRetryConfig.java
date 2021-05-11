package com.invest.coin.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.RetryableException;
import feign.Retryer;
import feign.codec.ErrorDecoder;

@Configuration
public class FeignRetryConfig {
	
	@Bean
    public Retryer retryer() {
        return new Retryer.Default(1000, 6000, 6);
    }
      
    @Bean
    public ErrorDecoder decoder() {
        return (methodKey, response) -> {
        	throw new RetryableException(
                    response.status(),
                    String.format("%s feign http request fail. Retry. - \nstatus: %s, \nheaders: %s, \nbody: %s, \nurl: %s", methodKey, response.status(), response.headers(), response.body(), response.request().url()),
                    response.request().httpMethod(),
                    null,
                    response.request());
        };
    }

}
