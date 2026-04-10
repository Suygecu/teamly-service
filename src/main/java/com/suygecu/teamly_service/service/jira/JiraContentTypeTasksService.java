package com.suygecu.teamly_service.service.jira;

import com.suygecu.teamly_service.dto.jira.JiraIssue;
import com.suygecu.teamly_service.dto.jira.JiraTaskRow;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JiraContentTypeTasksService {

	private final JiraRequestTypeContent jiraRequestTypeContent;
	private final JiraIssueMapper jiraIssueMapper;

	public JiraContentTypeTasksService(
			JiraRequestTypeContent jiraRequestTypeContent,
			JiraIssueMapper jiraIssueMapper
	) {
		this.jiraRequestTypeContent = jiraRequestTypeContent;
		this.jiraIssueMapper = jiraIssueMapper;
	}

	public Map<ContentType, List<JiraTaskRow>> loadTasksGroupedByContentType() {
		return Arrays.stream(ContentType.values())
				.collect(Collectors.toMap(
						contentType -> contentType,
						contentType -> loadTasksByContentType(contentType)
				));
	}

	public List<JiraTaskRow> loadTasksByContentType(ContentType contentType) {
		List<JiraIssue> issues = jiraRequestTypeContent.getIssueContent(
				contentType.getContentType(),
				jiraRequestTypeContent
		);

		return issues.stream()
				.map(jiraIssueMapper::toTaskRow)
				.collect(Collectors.toList());
	}
}
