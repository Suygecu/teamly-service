package com.suygecu.teamly_service.service.jira.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class HttpGetHelper {

	@Value("${jira.api.token}")
	private String apiToken;

	private final RestTemplate restTemplate = new RestTemplate();

	public String performGetRequest(String url) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(apiToken);
		headers.setAccept(List.of(MediaType.APPLICATION_JSON));

		HttpEntity<String> entity = new HttpEntity<>(null, headers);

		ResponseEntity<String> response = restTemplate.exchange(
				url,
				HttpMethod.GET,
				entity,
				String.class
		);
		//System.out.println("JSON Response: " + response.getBody());
		return response.getBody();
	}

	public String performPostRequest(String url, String jsonBody) {
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(apiToken);
		headers.setAccept(List.of(MediaType.APPLICATION_JSON));
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);

		ResponseEntity<String> response = restTemplate.exchange(
				url,
				HttpMethod.POST,
				entity,
				String.class
		);

		//System.out.println("JSON Response: " + response.getBody());
		return response.getBody();
	}
}