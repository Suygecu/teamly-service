package com.suygecu.teamly_service.teamly;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.suygecu.teamly_service.dto.TeamlyTokens;

import java.time.Instant;

@Service
public class TeamlyApiAuth {

	private final RestTemplate restTemplate = new RestTemplate();
	private final ObjectMapper mapper;
	private final TokenStorage tokenStorage;

	private static final String REFRESH_URL = EndpointsTeamly.AUTH_REFRESH.getUrl();

	public TeamlyApiAuth(ObjectMapper mapper, TokenStorage tokenStorage) {
		this.mapper = mapper;
		this.tokenStorage = tokenStorage;
	}


	public String requestNewToken(String clientId, String clientSecret) {
		try {
			String refreshToken = tokenStorage.readRefreshToken();

			TeamlyTokens request = new TeamlyTokens(
					clientId,
					clientSecret,
					refreshToken
			);

			String body = mapper.writeValueAsString(request);

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<String> entity = new HttpEntity<>(body, headers);

			ResponseEntity<String> response = restTemplate.exchange(
					REFRESH_URL,
					HttpMethod.POST,
					entity,
					String.class
			);

			JsonNode node = mapper.readTree(response.getBody());
			String accessToken = node.path("access_token").asText();
			String newRefreshToken = node.path("refresh_token").asText();

			tokenStorage.writeTokens(accessToken, newRefreshToken, Instant.now().toString());

			return accessToken;

		} catch (Exception e) {
			throw new RuntimeException("Ошибка при обновлении токена Teamly", e);
		}
	}
}
