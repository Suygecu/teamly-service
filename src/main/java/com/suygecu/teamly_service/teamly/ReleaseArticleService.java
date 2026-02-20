package com.suygecu.teamly_service.teamly;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.suygecu.teamly_service.dto.ArticleCreatePayload;
import com.suygecu.teamly_service.dto.CommandRequest;
import com.suygecu.teamly_service.dto.PropertyOperation;
import com.suygecu.teamly_service.dto.TeamlyUser;

import java.util.List;
import java.util.Map;

import static java.awt.SystemColor.text;

@Service
@RequiredArgsConstructor
public class ReleaseArticleService {

    private final TeamlyApiClient teamlyApiClient;
    private final ObjectMapper objectMapper;
    private final ReleaseSchemaService releaseSchemaService;

    public JsonNode createReleaseArticle(
            String contentDatabaseId,
            String spaceId,
            String parentId,
            String articleId,
            TeamlyUser owner,
            String titleText,
            String docsText,
            String commentText,
            String ruOptionId,
            String globalOptionId,
            List<TeamlyUser> reviewers,
            String jiraKey,
            String jiraUrl
    ) throws Exception {

        String titleCode       = releaseSchemaService.getCode(contentDatabaseId, "Название");
        String authorCode      = releaseSchemaService.getCode(contentDatabaseId, "Автор");
        String docsCode        = releaseSchemaService.getCode(contentDatabaseId, "Документация");
        String commentCode     = releaseSchemaService.getCode(contentDatabaseId, "Комментарий");
        String localeRuCode    = releaseSchemaService.getCode(contentDatabaseId, "Язык RU");
        String localeGlobalCode= releaseSchemaService.getCode(contentDatabaseId, "Язык Global");
        String reviewersCode   = releaseSchemaService.getCode(contentDatabaseId, "Ревьюеры");
        String jiraLinkCode    = releaseSchemaService.getCode(contentDatabaseId, "Задача Jira"); // пример

        PropertyOperation title = PropertyOps.add(titleCode, Map.of(
                "text", titleText,
                "icon", text
        ));

        PropertyOperation author = PropertyOps.update(authorCode, List.of(owner));
        PropertyOperation docs   = PropertyOps.add(docsCode, docsText);
        PropertyOperation comm   = PropertyOps.add(commentCode, commentText);
        PropertyOperation ru     = PropertyOps.add(localeRuCode, ruOptionId);
        PropertyOperation glob   = PropertyOps.add(localeGlobalCode, globalOptionId);
        PropertyOperation rev    = PropertyOps.add(reviewersCode, reviewers);
        PropertyOperation jira   = PropertyOps.add(jiraLinkCode, Map.of(
                "title", jiraKey,
                "url", jiraUrl
        ));

        ArticleCreatePayload.Entity entity = new ArticleCreatePayload.Entity();
        entity.setSpaceId(spaceId);
        entity.setIdUuid(articleId);
        entity.setParentId(parentId);
        entity.setProperties(List.of(
                title, author, docs, comm,
                ru, glob, rev, jira
        ));

        ArticleCreatePayload payload = new ArticleCreatePayload();
        payload.setEntity(entity);

        CommandRequest request = new CommandRequest();
        request.setCode("article_create");
        request.setPayload(payload);

        String response = teamlyApiClient.post(
                EndpointsTeamly.COMMAND_EXECUTE.getUrl(),
                request
        );

        return objectMapper.readTree(response);
    }
}
