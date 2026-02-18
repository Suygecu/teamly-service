package service.teamly;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class TokenStorage {

	private static final ObjectMapper mapper = new ObjectMapper();

	@Value("${teamly.path.file}")
	private Path tokenFile;

	public String readAccessToken(){

		try {
			if (!Files.exists(tokenFile)) {
				writeTokens("", "", "");
			}
			JsonNode node = mapper.readTree(Files.readString(tokenFile));
				return node.get("access_token").asText();

		} catch (Exception  e) {
			throw new RuntimeException("Ошибка чтения токена", e);
		}

	}



	public String readRefreshToken(){

		try {
			if (!Files.exists(tokenFile)) {
				writeTokens("", "", "");
			}
			JsonNode node = mapper.readTree(Files.readString(tokenFile));
			return node.get("refresh_token").asText();

		} catch (Exception  e) {
			throw new RuntimeException("Ошибка чтения токена", e);
		}

	}

	public void writeTokens(String accessToken, String refreshToken, String expiresAtIgnored) {
			System.out.println("Обновляю токены... Не выключай меня!");
		try {
			Path parentDir = tokenFile.getParent();
			if (parentDir != null && !Files.exists(parentDir)) {
				Files.createDirectories(parentDir);
			}
			ZonedDateTime nowLocal = ZonedDateTime.now(ZoneId.systemDefault());
			String formatted = nowLocal.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

			ObjectNode node = mapper.createObjectNode();
			node.put("access_token", accessToken);
			node.put("refresh_token", refreshToken);
			node.put("expires_at", formatted);

			Files.writeString(tokenFile, mapper.writerWithDefaultPrettyPrinter().writeValueAsString(node));
			System.out.println("Токен обновлён в " + formatted);
		} catch (Exception e) {
			throw new RuntimeException("Ошибка записи токена", e);
		}
	}

}
