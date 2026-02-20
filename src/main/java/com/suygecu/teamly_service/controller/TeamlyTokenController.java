package com.suygecu.teamly_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RestController
public class TeamlyTokenController {
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final Path tokenFilePath = Paths.get("build", "tmp", "com/suygecu/teamly_service/dto", "teamly_token.json");

	@GetMapping("/debug/teamly-token/pepsashepsa1337")
	public ResponseEntity<?> getTeamlyToken() {
		try {
			if (!Files.exists(tokenFilePath)) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("token file not found: " + tokenFilePath.toAbsolutePath());
			}

			String json = Files.readString(tokenFilePath);
			return ResponseEntity
					.ok()
					.contentType(MediaType.APPLICATION_JSON)
					.body(json);
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("error reading token file: " + e.getMessage());
		}
	}

	@PostMapping("/debug/teamly-token/pepsashepsa1337")
	public ResponseEntity<?> patchToken(@RequestBody Map<String, Object> updates) {
		try {
			ObjectNode root;
			if (Files.exists(tokenFilePath)) {
				String json = Files.readString(tokenFilePath);
				root = (ObjectNode) objectMapper.readTree(json);
			} else {
				root = objectMapper.createObjectNode();
			}
			updates.forEach((key, value) -> {
				if (value == null) {
					root.remove(key);
				} else {
					root.set(key, objectMapper.valueToTree(value));
				}
			});
			Files.createDirectories(tokenFilePath.getParent());
			String prettyJson = objectMapper
					.writerWithDefaultPrettyPrinter()
					.writeValueAsString(root);

			Files.writeString(tokenFilePath, prettyJson);

			return ResponseEntity.ok()
					.contentType(MediaType.APPLICATION_JSON)
					.body(prettyJson);

		} catch (Exception e) {
			return ResponseEntity.badRequest().body("invalid json: " + e.getMessage());
		}
	}
}
