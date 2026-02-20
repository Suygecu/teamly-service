package com.suygecu.teamly_service.teamly;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class TeamlyQueryService {

	private final TeamlyApiClient teamlyApiClient;
	private final com.fasterxml.jackson.databind.ObjectMapper objectMapper;

	public JsonNode fetchSchemaProperties (String contentDatabaseId, String parentId){

		 ObjectNode root = objectMapper.createObjectNode();
		 ObjectNode query = root.putObject("query");

		 ObjectNode filter = query.putObject("__filter");
		 filter.put("contentDatabaseId", contentDatabaseId);
		 filter.put("parentId", parentId);

		 ObjectNode content = query.putObject("content");
		 ObjectNode article  = content.putObject("article");
		 article.put("id", true);

		ObjectNode properties = article.putObject("properties");
		properties.put("properties", true);

		try {
			String responsetext = teamlyApiClient.post(EndpointsTeamly.CONTENT_TABLE.getUrl(), root);
			return objectMapper.readTree(responsetext);

		} catch (JsonProcessingException e) {
			throw new IllegalStateException("Failed to serialize Teamly payload", e);
		}

	}


	public boolean isComplete(JsonNode row){
		JsonNode props = row.path("article").path("properties").path("properties");

		String title = props.path("title").path("text").asText("").trim();
		String location = props.path("MLK0").asText("").trim();


		JsonNode from = props.path("LcZW");
		JsonNode to = props.path("8wZi");

		boolean fromField = from.isArray() && from.size() > 0;
		boolean toField = to.isArray() && to.size() > 0;

		return !title.isEmpty()
				&& !location.isEmpty()
				&& fromField
				&& toField;

	}

	public List<JsonNode> findCompleteRows (JsonNode response){
		JsonNode content = response.get("content");

		List<JsonNode> result = new ArrayList<>();
		for (JsonNode row : content) {
			if (isComplete(row))
				result.add(row);

		}
		return result;

	}

	public List<JsonNode> findNewCompleteRows(JsonNode response, Set<String> processedIds) {
		JsonNode content = response.get("content");

		List<JsonNode> result = new ArrayList<>();
		for (JsonNode row : content) {
			if (!isComplete(row)) continue;

			String articleId = row.path("article").path("id").asText("");
			if (articleId.isEmpty()) continue;

			if (!processedIds.contains(articleId)) {
				result.add(row);
			}
		}
		return result;
	}

}
