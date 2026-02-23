package com.suygecu.teamly_service;


import com.fasterxml.jackson.databind.JsonNode;
import com.suygecu.teamly_service.teamly.ProcessedIdsStore;
import com.suygecu.teamly_service.teamly.TeamlyQueryService;
import com.suygecu.teamly_service.teamly.TeamlyQuerySyncBuildTable;
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
	@Autowired
	private TeamlyQuerySyncBuildTable teamlyQuerySyncBuildTable;

	@Test
	void testFetchContentAndPrintCompleteRows() {
		String dbId = "0f98e8e4-98d6-4c63-93d6-185bab000f23";
		String parentId = "dba6aec9-a010-40b9-afc8-074467d5d0cb";



		// 2) получаем свежий контент
		JsonNode response = teamlyQuerySyncBuildTable.teamlyQuerySyncBuildTable(dbId, parentId);
		System.out.println(response);


	}
	/*@Autowired
	private MattermostNotifyService mattermostNotifyService;

	@Test
	void testMattermostSend() {
		mattermostNotifyService.sendNewRequest("Тестовая заявка " + "@moonshy");
	}*/



}
