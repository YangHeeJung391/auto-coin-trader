package com.invest.coin.domain.repository.coin;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.invest.coin.domain.entity.coin.VloatilityRangeBreakout;

public interface VloatilityRangeBreakoutRepository extends JpaRepository<VloatilityRangeBreakout, Long> {

	List<VloatilityRangeBreakout> findByCoinTypeAndDateStringAndDatetimeId(String coinType, String dateString, String datetimeId);

	List<VloatilityRangeBreakout> findByCoinTypeAndStatus(String coinType, String status);

	@Query("SELECT v FROM VloatilityRangeBreakout v WHERE v.coinType = :coinType AND v.datetimeId = :datetimeId AND v.dateString > :dateString")
	List<VloatilityRangeBreakout> findByCoinTypeAndDatetimeIdAndDateStringRange(
			@Param("coinType") String coinType,
			@Param("datetimeId") String datetimeId, 
			@Param("dateString") String dateString);

	List<VloatilityRangeBreakout> findByCoinTypeAndStatusAndIdLessThanEqual(String coinType, String status, long id);

}
