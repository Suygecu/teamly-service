package com.suygecu.teamly_service.teamly;

import org.springframework.http.HttpMethod;

public interface TeamlyEndpoint {
	String path();
	HttpMethod method();
}
