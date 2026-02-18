package service.teamly;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface TeamlyClient {
	public <T> String post(String url, T commandRequest) throws JsonProcessingException;
}
