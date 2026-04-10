package com.suygecu.teamly_service.dto.jira;

import java.util.List;

public class JiraIssueWithLinks {
    private final JiraIssue issue;
    private final List<JiraIssue> linkedIssues;

    public JiraIssueWithLinks(JiraIssue issue, List<JiraIssue> linkedIssues) {
        this.issue = issue;
        this.linkedIssues = linkedIssues;
    }

    public JiraIssue getMainIssue() { return issue; }
    public List<JiraIssue> getLinkedIssues() { return linkedIssues; }
}