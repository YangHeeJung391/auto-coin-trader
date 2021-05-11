package com.invest.coin.controller.webapi;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.invest.coin.domain.model.ErrorResponse;
import com.invest.coin.domain.service.quant.momentum.volatility_range_breakout.VloatilityRangeBreakoutBuyService;
import com.invest.coin.domain.service.quant.momentum.volatility_range_breakout.VloatilityRangeBreakoutStoplossService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ErrorController {
	
	private final VloatilityRangeBreakoutBuyService vloatilityRangeBreakoutBuyService;
	private final VloatilityRangeBreakoutStoplossService vloatilityRangeBreakoutStoplossService;
	
	@ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(HttpServletRequest request, Exception e) {
		log.error("Request: " + request.getRequestURL());
        log.error("handleException", e);

        ErrorResponse response
                = ErrorResponse
                        .builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .message(e.toString())
                        .build();

        vloatilityRangeBreakoutBuyService.setChecking(false);
        vloatilityRangeBreakoutStoplossService.setChecking(false);
        
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
