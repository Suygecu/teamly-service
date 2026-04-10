package com.suygecu.teamly_service.controller;

import com.suygecu.teamly_service.dto.jira.JiraTaskRow;
import com.suygecu.teamly_service.service.jira.ContentType;
import com.suygecu.teamly_service.service.jira.JiraContentTypeTasksService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class JiraTestController {

	private final JiraContentTypeTasksService jiraContentTypeTasksService;

	public JiraTestController(JiraContentTypeTasksService jiraContentTypeTasksService) {
		this.jiraContentTypeTasksService = jiraContentTypeTasksService;
	}

	@GetMapping("/debug/jira/content-types")
	public Map<ContentType, List<JiraTaskRow>> getAllTasks() {
		return jiraContentTypeTasksService.loadTasksGroupedByContentType();
	}

	@GetMapping("/debug/jira/concept")
	public List<JiraTaskRow> getConceptTasks() {
		return jiraContentTypeTasksService.loadTasksByContentType(ContentType.CONCEPT);
	}
}
