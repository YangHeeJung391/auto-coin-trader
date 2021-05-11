package com.invest.coin.domain.entity.coin;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Entity
@Table(name = "vloatility_range_breakout")
public class VloatilityRangeBreakout {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;
	
	@Column(name="date_string")
	private String dateString;
	
	@Column(name="datetime_id")
	private String datetimeId;
	
	@Column(name="coin_type")
	private String coinType;
	
	@Column(name="opening_price", precision = 10, scale = 8)
	private BigDecimal openingPrice;
	
	@Column(name="target_price", precision = 10, scale = 8)
	private BigDecimal targetPrice;
	
	@Column(name="target_count", precision = 10, scale = 8)
	private BigDecimal targetCount;

	@Column(name = "buy_uuid")
	private String buyUuid;
	
	@Column(name="buy_price", precision = 10, scale = 8)
	private BigDecimal buyPrice;
	
	@Column(name="buy_count", precision = 10, scale = 8)
	private BigDecimal buyCount;

	@Column(name = "sell_uuid")
	private String sellUuid;
	
	@Column(name="sell_price", precision = 10, scale = 8)
	private BigDecimal sellPrice;
	
	@Column(name="sell_count", precision = 10, scale = 8)
	private BigDecimal sellCount;
	
	@Column(name="fee", precision = 10, scale = 8)
	private BigDecimal fee;
	
	@Column(name="noise_rate", precision = 10, scale = 8)
	private BigDecimal noiseRate;
	
	@Column(name="moving_avg_score", precision = 10, scale = 8)
	private BigDecimal movingAvgScore;
	
	@Column(name="vloatility_invest_rate", precision = 10, scale = 8)
	private BigDecimal vloatilityInvestRate;
	
	@Column(name="k", precision = 10, scale = 8)
	private BigDecimal k;
	
	@Column(name = "status")
	private String status;
	
	@Column(name="created_at")
	private ZonedDateTime createdAt;
	
	@Column(name="updated_at")
	private ZonedDateTime updatedAt;
	
	@Column(name="buy_at")
	private ZonedDateTime buyAt;
	
	@Column(name="sell_at")
	private ZonedDateTime sellAt;

}
