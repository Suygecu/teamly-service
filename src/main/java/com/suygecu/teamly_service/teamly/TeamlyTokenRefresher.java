package com.suygecu.teamly_service.teamly;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class TeamlyTokenRefresher {

	private final TeamlyApiAuth teamlyApiAuth;

	@Value("${teamly.client.id}")
	private String clientId;

	@Value("${teamly.client.secret}")
	private String clientSecret;

	public TeamlyTokenRefresher(TeamlyApiAuth teamlyApiAuth) {
		this.teamlyApiAuth = teamlyApiAuth;
	}

	@Scheduled(fixedRate = 5 * 60 * 60 * 1000, initialDelay = 60_000)
	@Async
	public void refreshTokenEveryMinute() {
		System.out.println("[SCHEDULER] Автообновление токена Teamly...");
		try {
			String newAccessToken = teamlyApiAuth.requestNewToken(clientId, clientSecret);
			System.out.println("[SCHEDULER] Новый токен получен: " + newAccessToken.substring(0, 20) + "...");
		} catch (Exception e) {
			System.err.println("[SCHEDULER] Ошибка при обновлении токена: " + e.getMessage());
		}
	}
}

