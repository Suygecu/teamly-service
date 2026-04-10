package com.suygecu.teamly_service.service.jira;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.suygecu.teamly_service.dto.jira.Fields;
import com.suygecu.teamly_service.dto.jira.JiraIssue;
import com.suygecu.teamly_service.dto.jira.JiraSearchResult;
import com.suygecu.teamly_service.service.jira.util.HttpGetHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Component("jiraRequestTypeContent")
public  class JiraRequestTypeContent implements IssueQueryProvider {

    @Value("${jira.base.url}")
    private String baseUrl;

    private final HttpGetHelper httpHelper;
    private final ObjectMapper mapper = new ObjectMapper();

    public JiraRequestTypeContent(HttpGetHelper httpHelper) {
        this.httpHelper = httpHelper;
    }

    @Override
    public String getJql(String contentType, List<String> statuses) {
        String joinedStatuses = statuses.stream()
                .map(s -> "\"" + s + "\"")
                .collect(Collectors.joining(", "));

        return "issuetype in (Task) AND status in (" +
                joinedStatuses + ") AND status != DONE AND ContentType in (\"" + contentType + "\")";
    }

    public synchronized List<JiraIssue> getIssueContent(String contentType, IssueQueryProvider queryProvider) {
        List<JiraIssue> allIssues = new ArrayList<>();
        String url = baseUrl + "/rest/api/2/search";

        List<String> statuses = List.of("TO DO", "PAUSE", "Preparing", "BACKLOG", "IN DEVELOPMENT");
        String jql = queryProvider.getJql(contentType, statuses);

        ObjectNode body = mapper.createObjectNode();
        body.put("jql", jql);
        body.put("startAt", 0);
        body.put("maxResults", 5000);

        try {
            String jsonBody = mapper.writeValueAsString(body);
            String response = httpHelper.performPostRequest(url, jsonBody);
            JiraSearchResult result = mapper.readValue(response, JiraSearchResult.class);
            allIssues.addAll(result.getIssues());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }

        return allIssues;
    }

    @Override
    public String getJql(String issueKey) {
        return "";
    }
}