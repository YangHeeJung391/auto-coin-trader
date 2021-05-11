package com.invest.coin.domain.model.upbit.market;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UpbitMinuteCandle {
	@JsonProperty("market") 
	private String market;
    @JsonProperty("candle_date_time_utc")
    private String candleDateTimeUtc;
    @JsonProperty("candle_date_time_kst")
    private String candleDateTimeKst;
    @JsonProperty("opening_price")
    private BigDecimal openingPrice;
    @JsonProperty("high_price")
    private BigDecimal highPrice;
    @JsonProperty("low_price")
    private BigDecimal lowPrice;
    @JsonProperty("trade_price")
    private BigDecimal tradePrice;
    @JsonProperty("timestamp")
    private long timestamp;
    @JsonProperty("candle_acc_trade_price")
    private BigDecimal candleAccTradePrice;
    @JsonProperty("candle_acc_trade_volume")
    private BigDecimal candleAccTradeVolume;

}
