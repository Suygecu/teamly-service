package com.suygecu.teamly_service.teamly;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamlyQuerySyncBuildService {

	private final TeamlyApiClient teamlyApiClient;
	private final ObjectMapper objectMapper;
	@Value("${teamly.content.database.syncbuild.id}")
	private String dbId;
	@Value("${teamly.content.database.syncbuild.parent.id}")
	private String parentId;

	public JsonNode teamlyQuerySyncBuildTable(String contentDatabaseId, String parentId) {

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
		} catch (JsonProcessingException e) {
			throw new IllegalStateException("Failed to parse Teamly response JSON", e);
		} catch (Exception e) {
			throw new IllegalStateException("Teamly request failed", e);
		}

	}

	public List<String> collectTitlesWithBadStatuses(JsonNode response) {
		List<String> badTitles = new ArrayList<>();
		if (response == null || response.isNull()) return badTitles;

		List<JsonNode> articles = response.findValues("article");

		for (JsonNode article : articles) {
			JsonNode props = article.path("properties").path("properties");
			if (!props.isObject()) continue;

			JsonNode gpnbNode = props.get("GpNB");
			JsonNode mt8hNode = props.get("Mt8H");
			JsonNode tnsjNode = props.get("tnsj");

			// Если хотя бы одного статуса нет — игнорим строку
			if (isMissingOrNull(gpnbNode) || isMissingOrNull(mt8hNode) || isMissingOrNull(tnsjNode)) {
				continue;
			}

			String gpnb = textOrNull(gpnbNode);
			String mt8h = textOrNull(mt8hNode);
			String tnsj = textOrNull(tnsjNode);

			// пустые — игнорим строку
			if (isBlank(gpnb) || isBlank(mt8h) || isBlank(tnsj)) {
				continue;
			}

			boolean hasBad = !SyncBuildTableStatus.isAcceptedId(gpnb) || !SyncBuildTableStatus.isAcceptedId(
					mt8h) || !SyncBuildTableStatus.isAcceptedId(tnsj);

			if (hasBad) {
				String title = props.path("title").path("text").asText("");
				if (!title.isBlank()) {
					badTitles.add(title); // title уже содержит запятые — НЕ трогаем
				}
			}
		}

		return getCleanedList(badTitles);
	}

	private static boolean isMissingOrNull(JsonNode node) {
		return node == null || node.isNull() || node.isMissingNode();
	}

	private static String textOrNull(JsonNode node) {
		return (node == null || node.isNull() || node.isMissingNode()) ? null : node.asText(null);
	}

	private static boolean isBlank(String s) {
		return s == null || s.isBlank();
	}

	public List<String> getCleanedList(List<String> badTitles) {

		return badTitles.stream().filter(s -> s != null && !s.isBlank()).map(String::trim).map(
				s -> s.replaceAll(",+$", "")).filter(s -> !s.isBlank()).distinct().collect(Collectors.toList());
	}

	public String requestSyncBuildTable() {
		JsonNode response = teamlyQuerySyncBuildTable(dbId, parentId);
		List<String> badTitles = collectTitlesWithBadStatuses(response);
		return String.join(",", badTitles);
	}
}
