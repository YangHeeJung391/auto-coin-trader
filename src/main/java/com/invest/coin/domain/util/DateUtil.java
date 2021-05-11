package com.invest.coin.domain.util;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
	
	public static String getDateString(ZonedDateTime zonedDateTime){
		return zonedDateTime.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		
	}

}
