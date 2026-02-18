package com.suygecu.teamly_service.teamly;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TeamlyApiClient implements TeamlyClient {


	private final RestTemplate restTemplate = new RestTemplate();
	private final ObjectMapper mapper;
	private final TokenStorage tokenStorage;



	public TeamlyApiClient(ObjectMapper mapper, TokenStorage tokenStorage) {
		this.mapper = mapper;
		this.tokenStorage = tokenStorage;
	}


	@Override
	public <T> String post(String url, T payload) throws JsonProcessingException {

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(tokenStorage.readAccessToken());

		String body = mapper.writeValueAsString(payload);
		HttpEntity<String> entity = new HttpEntity<>(body, headers);

		return restTemplate.postForEntity(url, entity, String.class).getBody();
	}

}
