package com.suygecu.teamly_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class ArticleCreatePayload {
	private Entity entity;

	@Data
	public static class Entity {
		private String spaceId;
		private String idUuid;
		private String parentId;  // для дочерней строки
		private List<PropertyOperation> properties;
	}
}
