package com.suygecu.teamly_service.service.jira;

import com.suygecu.teamly_service.dto.jira.CustomFieldOption;
import com.suygecu.teamly_service.dto.jira.Fields;
import com.suygecu.teamly_service.dto.jira.JiraIssue;
import com.suygecu.teamly_service.dto.jira.JiraTaskRow;
import org.springframework.stereotype.Component;

@Component
public class JiraIssueMapper {

	public JiraTaskRow toTaskRow(JiraIssue issue) {
		Fields fields = issue.getFields();

		return JiraTaskRow.builder()
				.priority(fields != null && fields.getPriority() != null ? fields.getPriority().getName() : null)
				.key(issue.getKey())
				.summary(fields != null ? fields.getSummary() : null)
				.status(fields != null && fields.getStatus() != null ? fields.getStatus().getName() : null)
				.assignee(fields != null && fields.getAssignee() != null ? fields.getAssignee().getDisplayName() : null)
				.targetEnd(fields != null ? fields.getTargetEnd() : null)
				.epicKey(fields != null ? fields.getEpicLink() : null)
				.epicTitle(fields != null ? fields.getEpicTitle() : null)
				.contentType(extractContentType(fields))
				.leadTime(fields != null ? fields.getLeadTime() : null)
				.build();
	}

	private String extractContentType(Fields fields) {
		if (fields == null) {
			return null;
		}

		CustomFieldOption option = fields.getContentType();
		if (option == null) {
			return null;
		}

		return option.getValue();
	}
}