package com.suygecu.teamly_service;


import com.fasterxml.jackson.databind.JsonNode;
import com.suygecu.teamly_service.teamly.ProcessedIdsStore;
import com.suygecu.teamly_service.teamly.TeamlyQueryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;

import com.release.jira_api_release.service.MattermostNotifyService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TeamlyQueryServiceIT {

	@Autowired
	private TeamlyQueryService teamlyQueryService;
	@Autowired
	private ProcessedIdsStore processedIdsStore;

	/*@Test
	void testFetchContentAndPrintCompleteRows() {
		String dbId = "6f39f073-928d-4deb-bb20-4dd994e6334a";
		String parentId = "a3591af8-c9fa-4eba-894d-18a19bc2f122";

		Set<String> processed = processedIdsStore.read();

		// 2) получаем свежий контент
		JsonNode response = teamlyQueryService.fetchSchemaProperties(dbId, parentId);

		// 3) находим новые полные
		List<JsonNode> newComplete = teamlyQueryService.findNewCompleteRows(response, processed);

		System.out.println("NEW complete count = " + newComplete.size());

		// 4) печатаем и добавляем в processed
		for (JsonNode row : newComplete) {
			String id = row.path("article").path("id").asText("");
			String title = row.path("article").path("properties").path("properties").path("title").path("text").asText(
					"");
			System.out.println("NEW COMPLETE id=" + id + " title=" + title);

			processed.add(id);
		}

		// 5) сохраняем обновлённый список
		processedIdsStore.write(processed);
	}*/
	@Autowired
	private MattermostNotifyService mattermostNotifyService;

	@Test
	void testMattermostSend() {
		mattermostNotifyService.sendNewRequest("Тестовая заявка " + "@moonshy");
	}
}
