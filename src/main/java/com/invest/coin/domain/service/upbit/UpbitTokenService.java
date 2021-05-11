package com.invest.coin.domain.service.upbit;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpbitTokenService {
	
	@Value("${upbit.accessKey}")
	private String accessKey;

	@Value("${upbit.secretKey}")
	private String secretKey;
	
	public String createAuthToken() {
		String token =
				JWT.create()
				.withClaim("access_key", accessKey)
				.withClaim("nonce", UUID.randomUUID().toString())
				.sign(Algorithm.HMAC256(secretKey));
		return String.format("Bearer %s", token);
	}
	
	public String createAuthToken(String queryString) {
		String queryHash = "";
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(queryString.getBytes("utf8"));
			queryHash = String.format("%0128x", new BigInteger(1, md.digest()));
			
		} catch (NoSuchAlgorithmException e) {
		} catch (UnsupportedEncodingException e) {
		}
		
		String token =
				JWT.create()
				.withClaim("access_key", accessKey)
				.withClaim("nonce", UUID.randomUUID().toString())
				.withClaim("query_hash", queryHash)
				.withClaim("query_hash_alg", "SHA512")
				.sign(Algorithm.HMAC256(secretKey));
		return String.format("Bearer %s", token);
	}

}
