package service.teamly;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReleaseSchemaService {

    private final TeamlyApiClient teamlyApiClient;
    private final ObjectMapper mapper;


    private final Map<String, Map<String, String>> cache = new HashMap<>();

    public Map<String, String> getNameToCode(String contentDatabaseId) throws Exception {
        if (cache.containsKey(contentDatabaseId)) {
            return cache.get(contentDatabaseId);
        }

        Map<String, Object> body = buildQueryBody(contentDatabaseId);

        String response = teamlyApiClient.post(
                EndpointsTeamly.CONTENT_TABLE.getUrl(),
                body
        );

        JsonNode root = mapper.readTree(response);


        JsonNode propsArray = root
                .path("schemaProperties");

        Map<String, String> map = new HashMap<>();

        for (JsonNode node : propsArray) {
            String name = node.path("name").asText();
            String code = node.path("code").asText();
            if (!name.isEmpty() && !code.isEmpty()) {
                map.put(name, code);
            }
        }

        cache.put(contentDatabaseId, map);
        return map;
    }

    public String getCode(String contentDatabaseId, String propertyName) throws Exception {
        Map<String, String> codes = getNameToCode(contentDatabaseId);
        return codes.get(propertyName);
    }

    private Map<String, Object> buildQueryBody(String contentDatabaseId) {
        Map<String, Object> filter = Map.of(
                "contentDatabaseId", contentDatabaseId
        );

        Map<String, Object> schemaProps = Map.of(
                "id", true,
                "spaceId", true,
                "propertyId", true,
                "name", true,
                "type", true,
                "code", true,
                "format", true,
                "options", true
        );

        Map<String, Object> query = Map.of(
                "__filter", filter,
                "schemaProperties", schemaProps
        );

        return Map.of("query", query);
    }
}
