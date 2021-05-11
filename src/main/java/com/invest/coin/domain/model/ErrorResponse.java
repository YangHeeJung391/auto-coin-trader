package com.invest.coin.domain.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
	
	private LocalDateTime timestamp = LocalDateTime.now();
    private String message;
    private String code;
    private int status;

}
