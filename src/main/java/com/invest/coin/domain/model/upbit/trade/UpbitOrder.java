package com.invest.coin.domain.model.upbit.trade;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.invest.coin.domain.model.upbit.UpbitError;

import lombok.Data;

@Data
public class UpbitOrder {
	@JsonProperty("error")
	private UpbitError error;
	@JsonProperty("uuid")
	private String uuid;
	private String side;
	@JsonProperty("ord_type")
	private String orderType;
	private BigDecimal price;
	@JsonProperty("avg_price")
	private BigDecimal avgPrice;
	private String state;
	private String market;
	@JsonProperty("created_at")
	private ZonedDateTime createdAt;
	private BigDecimal volume;
	@JsonProperty("remaining_volume")
	private BigDecimal remainingVolume;
	@JsonProperty("reserved_fee")
	private BigDecimal reservedFee;
	@JsonProperty("remaining_fee")
	private BigDecimal remainingFee;
	@JsonProperty("paid_fee")
	private BigDecimal paidFee;
	private BigDecimal locked;
	@JsonProperty("executed_volume")
	private BigDecimal executedVolume;
	@JsonProperty("trade_count")
	private int tradesCount;
	@JsonProperty("trades")
	private List<UpbitOrderTrade> trades;


}
