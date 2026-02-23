package com.suygecu.teamly_service.teamly;

import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum SyncBuildTableStatus {
	ACCEPTED_TGD("1eca2a27-dd2e-477e-8160-39f7db65dc4a"),
	ACCEPTED_LD("aaae1994-0fb1-43be-aabd-959931baf7df"),
	ACEPTED_LA("1465052c-d16b-40a1-9336-bff807a7ebcd");

	private final String teamlyId;


	SyncBuildTableStatus(String teamlyId) {
		this.teamlyId = teamlyId;
	}

	private static final Set<String> ACCEPTED_IDS =
			Stream.of(values()).map(SyncBuildTableStatus::getTeamlyId).collect(Collectors.toSet());

	public static boolean isAcceptedId(String id) {
		return id != null && ACCEPTED_IDS.contains(id);
	}
}


