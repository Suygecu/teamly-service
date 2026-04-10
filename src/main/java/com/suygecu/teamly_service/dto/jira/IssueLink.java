package com.suygecu.teamly_service.dto.jira;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IssueLink {
    private IssueType type;
    private JiraIssue inwardIssue;
}
