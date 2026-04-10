package com.suygecu.teamly_service.dto.jira;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JiraTaskRow {
	private String priority;
	private String key;
	private String summary;
	private String status;
	private String assignee;
	private String targetEnd;
	private String epicKey;
	private String epicTitle;
	private String contentType;
	private String leadTime;

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getTargetEnd() {
		return targetEnd;
	}

	public void setTargetEnd(String targetEnd) {
		this.targetEnd = targetEnd;
	}

	public String getEpicKey() {
		return epicKey;
	}

	public void setEpicKey(String epicKey) {
		this.epicKey = epicKey;
	}

	public String getEpicTitle() {
		return epicTitle;
	}

	public void setEpicTitle(String epicTitle) {
		this.epicTitle = epicTitle;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getLeadTime() {
		return leadTime;
	}

	public void setLeadTime(String leadTime) {
		this.leadTime = leadTime;
	}
}
