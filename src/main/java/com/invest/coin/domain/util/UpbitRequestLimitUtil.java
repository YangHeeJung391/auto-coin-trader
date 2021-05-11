package com.invest.coin.domain.util;

import java.time.ZonedDateTime;

import org.springframework.http.HttpHeaders;

public class UpbitRequestLimitUtil {
	
	public static void checkRequestAvailable(HttpHeaders httpHeaders) {
		String remainingReq = httpHeaders.get("remaining-req").get(0);
		String[] splitedRemainingReq = remainingReq.split(";");
		String[] splitedRemainingReqMin = splitedRemainingReq[1].split("=");
		String[] splitedRemainingReqSec = splitedRemainingReq[2].split("=");
		int remainingReqmin = Integer.parseInt(splitedRemainingReqMin[1]);
		int remainingReqsec = Integer.parseInt(splitedRemainingReqSec[1]);
		if (remainingReqmin < 3) {
			ZonedDateTime now = ZonedDateTime.now();
			while (now.getMinute() == ZonedDateTime.now().getMinute()){
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} else if (remainingReqsec < 5) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
