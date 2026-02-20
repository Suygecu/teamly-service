package com.suygecu.teamly_service.teamly;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

@Service
public class PendingIdsStore {

	private final ObjectMapper objectMapper;

	private final Path file = Path.of(
			System.getProperty("user.dir"),
			"build", "tmp", "snapshots", "pending_ids.json"
	);

	public PendingIdsStore(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	public Set<String> read() {
		try {
			Files.createDirectories(file.getParent());
			if (!Files.exists(file)) {
				write(new HashSet<>());
				return new HashSet<>();
			}
			String text = Files.readString(file);
			if (text == null || text.isBlank()) return new HashSet<>();
			return objectMapper.readValue(text, new TypeReference<Set<String>>() {});
		} catch (IOException e) {
			throw new IllegalStateException("Failed to read pending ids: " + file.toAbsolutePath(), e);
		}
	}

	public void write(Set<String> ids) {
		try {
			Files.createDirectories(file.getParent());
			Files.writeString(file, objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(ids));
		} catch (IOException e) {
			throw new IllegalStateException("Failed to write pending ids: " + file.toAbsolutePath(), e);
		}
	}
}
