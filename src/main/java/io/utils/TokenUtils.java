package io.utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import io.smallrye.jwt.build.Jwt;

public class TokenUtils {
	// public static String generateDamlTokenString(final String User) {

	// Map<String, Object> obj = Map.of("https://daml.com/ledger-api",
	// Map.of("ledgerId", "dapLedger",
	// "applicationId", "DAML",
	// "actAs", List.of(User)));
	// // [2021-08-30 16:33:39] jwt기본값들이 자동으로 생성됨, 빼나마나..
	// return Jwt.claims(obj)
	// .audience("https://daml.com/ledger-api")
	// .upn("daml application")
	// .groups("daml")
	// .issuedAt(Instant.now().toEpochMilli())
	// .expiresAt(Instant.now().plus(2, ChronoUnit.DAYS))
	// .sign();
	// }

	public static String generateDamlTokenString(final String User) {
		return Jwt.claims()
				.subject(User)
				.claim("scope", "daml_ledger_api")
				.signWithSecret("generateDamlTokenStringgenerateDamlTokenStringgenerateDamlTokenStringgenerateDamlTokenStringgenerateDamlTokenString");
	}
}