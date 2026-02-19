package service.teamly;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.suygecu.teamly_service.teamly.EndpointsTeamly;
import com.suygecu.teamly_service.teamly.TeamlyApiClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class TeamlySyncRequestPoller {

	private final TeamlyApiClient teamlyApiClient;
	private final ObjectMapper objectMapper;
	private final MattermostSyncNotifier mattermostSyncNotifier;

	@Value("${teamly.sync.content-database-id:6f39f073-928d-4deb-bb20-4dd994e6334a}")
	private String contentDatabaseId;

	@Value("${teamly.sync.parent-id:280625bf-b442-4bdf-a095-5be014907910}")
	private String parentId;

	@Value("${teamly.sync.dump-file:build/teamly/sync-requests-log.jsonl}")
	private Path dumpFile;

	private final Set<String> knownRequestIds = new LinkedHashSet<>();
	private volatile boolean initialized = false;

	public TeamlySyncRequestPoller(TeamlyApiClient teamlyApiClient,
	                               ObjectMapper objectMapper,
	                               MattermostSyncNotifier mattermostSyncNotifier) {
		this.teamlyApiClient = teamlyApiClient;
		this.objectMapper = objectMapper;
		this.mattermostSyncNotifier = mattermostSyncNotifier;
	}

	@Scheduled(fixedDelayString = "${teamly.sync.poll-interval-ms:1000}", initialDelayString = "${teamly.sync.initial-delay-ms:5000}")
	public void pollSyncRequests() {
		try {
			Map<String, Object> payload = buildPayload();
			String response = teamlyApiClient.post(EndpointsTeamly.CONTENT_TABLE.getUrl(), payload);
			JsonNode responseJson = objectMapper.readTree(response);

			List<JsonNode> items = extractItems(responseJson);
			appendDump(responseJson, items.size());

			Set<String> currentIds = new LinkedHashSet<>();
			Map<String, String> titleById = new LinkedHashMap<>();

			for (JsonNode item : items) {
				String id = item.path("id").asText("");
				if (id.isBlank()) {
					continue;
				}
				currentIds.add(id);
				titleById.put(id, item.path("title").asText("(без названия)"));
			}

			if (!initialized) {
				knownRequestIds.clear();
				knownRequestIds.addAll(currentIds);
				initialized = true;
				System.out.println("[TEAMLY_SYNC] Инициализация завершена. Найдено запросов: " + currentIds.size());
				return;
			}

			Set<String> newIds = new LinkedHashSet<>(currentIds);
			newIds.removeAll(knownRequestIds);

			if (!newIds.isEmpty()) {
				StringBuilder text = new StringBuilder();
				text.append("📥 *Новый запрос на синхрон (Teamly):*\n");

				int limit = 5;
				int i = 0;
				for (String id : newIds) {
					if (i >= limit) {
						break;
					}
					text.append("- ").append(titleById.getOrDefault(id, "(без названия)"))
							.append(" (`").append(id).append("`)\n");
					i++;
				}

				if (newIds.size() > limit) {
					text.append("- ...и еще ").append(newIds.size() - limit).append("\n");
				}

				text.append("\n🔗 https://base.xexbo.ru/space/dba6aec9-a010-40b9-afc8-074467d5d0cb/database/")
						.append(contentDatabaseId)
						.append("?viewId=663fba82-0ed7-40de-a1a7-21c542ec3e6a");

				mattermostSyncNotifier.notifyNewSyncRequest(text.toString());
				System.out.println("[TEAMLY_SYNC] Отправлено уведомление о новых запросах: " + newIds.size());
			}

			knownRequestIds.clear();
			knownRequestIds.addAll(currentIds);
		} catch (Exception e) {
			System.err.println("[TEAMLY_SYNC] Ошибка при опросе таблицы Teamly: " + e.getMessage());
		}
	}

	private Map<String, Object> buildPayload() {
		Map<String, Object> filter = Map.of(
				"contentDatabaseId", contentDatabaseId,
				"parentId", parentId
		);

		Map<String, Object> author = Map.of(
				"avatar", Map.of("path", true),
				"full_name", true,
				"external_id", true,
				"id", true
		);

		Map<String, Object> schemaProperties = new LinkedHashMap<>();
		schemaProperties.put("id", true);
		schemaProperties.put("spaceId", true);
		schemaProperties.put("propertyId", true);
		schemaProperties.put("name", true);
		schemaProperties.put("type", true);
		schemaProperties.put("code", true);
		schemaProperties.put("format", true);
		schemaProperties.put("options", true);
		schemaProperties.put("protected", true);
		schemaProperties.put("hide", true);
		schemaProperties.put("sort", true);
		schemaProperties.put("version", true);
		schemaProperties.put("versionAt", true);
		schemaProperties.put("createdBy", true);
		schemaProperties.put("updatedBy", true);
		schemaProperties.put("createdAt", true);
		schemaProperties.put("updatedAt", true);
		schemaProperties.put("commands", true);

		Map<String, Object> query = new LinkedHashMap<>();
		query.put("__filter", filter);
		query.put("id", true);
		query.put("title", true);
		query.put("author", author);
		query.put("schemaProperties", schemaProperties);

		return Map.of("query", query);
	}

	private List<JsonNode> extractItems(JsonNode root) {
		List<JsonNode> items = new ArrayList<>();

		JsonNode candidates = root.path("content");
		if (candidates.isArray()) {
			candidates.forEach(items::add);
		}

		if (items.isEmpty()) {
			candidates = root.path("data");
			if (candidates.isArray()) {
				candidates.forEach(items::add);
			}
		}

		if (items.isEmpty()) {
			candidates = root.path("items");
			if (candidates.isArray()) {
				candidates.forEach(items::add);
			}
		}

		if (items.isEmpty() && root.isArray()) {
			root.forEach(items::add);
		}

		return items;
	}

	private void appendDump(JsonNode responseJson, int itemsCount) {
		try {
			Path parent = dumpFile.getParent();
			if (parent != null) {
				Files.createDirectories(parent);
			}

			ObjectNode line = objectMapper.createObjectNode();
			line.put("fetchedAt", OffsetDateTime.now().toString());
			line.put("itemsCount", itemsCount);
			line.set("data", responseJson);

			Files.writeString(
					dumpFile,
					objectMapper.writeValueAsString(line) + System.lineSeparator(),
					java.nio.file.StandardOpenOption.CREATE,
					java.nio.file.StandardOpenOption.APPEND
			);
		} catch (Exception e) {
			System.err.println("[TEAMLY_SYNC] Ошибка записи дампа в файл: " + e.getMessage());
		}
	}
}
