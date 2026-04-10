package com.suygecu.teamly_service.service.jira;

import java.util.List;

public interface IssueQueryProvider {
    String getJql(String issueKey);
    String getJql(String issueKey, List<String> statuses);
}
