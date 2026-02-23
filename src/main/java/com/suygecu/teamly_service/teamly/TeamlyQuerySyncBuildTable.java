package com.suygecu.teamly_service.teamly;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamlyQuerySyncBuildTable {

	private final TeamlyApiClient teamlyApiClient;
	private final ObjectMapper objectMapper;

	public JsonNode teamlyQuerySyncBuildTable(String contentDatabaseId, String parentId){

		ObjectNode root = objectMapper.createObjectNode();
		ObjectNode query = root.putObject("query");

		ObjectNode filter = query.putObject("__filter");
		filter.put("contentDatabaseId", contentDatabaseId);
		filter.put("parentId", parentId);

		ObjectNode content = query.putObject("content");
		ObjectNode article = content.putObject("article");
		article.put("id", true);

		ObjectNode properties = article.putObject("properties");
		properties.put("properties", true);

		try {
			String responseText = teamlyApiClient.post(EndpointsTeamly.CONTENT_TABLE.getUrl(), root);
			return objectMapper.readTree(responseText);
		}catch (JsonProcessingException e) {
			throw new IllegalStateException("Failed to serialize Teamly payload", e);

		}

	}

}
