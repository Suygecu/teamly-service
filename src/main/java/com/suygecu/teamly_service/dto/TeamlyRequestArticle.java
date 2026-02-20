package com.suygecu.teamly_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TeamlyRequestArticle {

	@JsonProperty("query")
	private Query query;

	@Data
	public static class Query {
		@JsonProperty("__filter")
		private Filter filter;

		private boolean title;
		private boolean icon;
		private Properties properties;
		private boolean latestProperties;
		private Activity activity;
		private boolean allow_comments;
		private boolean anchored;
		private boolean archived;
		private boolean attachmentCount;
		private boolean autoPublication;
		private Author author;
		private Badges badges;
		private EditorContentObject editorContentObject;
		private boolean breadcrumbs;
		private Classifiers classifiers;
		private Comments comments;
		private Cover cover;
		private boolean created_at;
		private boolean disabledActions;
		private boolean icon_color;
		private boolean id;
		private boolean is_hidden;
		private boolean is_official_doc;
		private Likes likes;
		private boolean linkCount;
		private Preview preview;
		private PublicationStatus publication_status;
		private boolean relatedParentId;
		private Space space;
		private boolean space_id;
		private Tags tags;
		private boolean updated_at;
		private Views views;
		private SchemaProperties schemaProperties;
	}

	@Data
	public static class Filter {
		private String id;
		private Long editorContentAfterVersionAt;
	}

	@Data
	public static class Properties {
		private boolean properties;
	}

	@Data
	public static class Activity {
		private boolean from;
		private boolean to;
	}

	@Data
	public static class Author {
		private Avatar avatar;
		private boolean full_name;
		private boolean external_id;
		private boolean id;
	}

	@Data
	public static class Avatar {
		private boolean path;
	}

	@Data
	public static class Badges {
		private boolean name;
		private boolean badge_code;
		private boolean description;
		private boolean active_to;
		private boolean meta;
	}

	@Data
	public static class EditorContentObject {
		private boolean content;
		private boolean versionAt;
	}

	@Data
	public static class Classifiers {
		private boolean id;
		private boolean title;
	}

	@Data
	public static class Comments {
		private boolean count;
	}

	@Data
	public static class Cover {
		private boolean id;
		private boolean settings;
		private boolean source_id;
	}

	@Data
	public static class Likes {
		private boolean count;
	}

	@Data
	public static class Preview {
		private boolean text;
		private boolean decoded_text;
	}

	@Data
	public static class PublicationStatus {
		private boolean id;
		private boolean name;
		private boolean slug;
	}

	@Data
	public static class Space {
		private Container container;
		private SchemaProperties schemaProperties;
		private boolean main_article_id;
		private boolean keeping_type;
		private boolean cardLayout;
		private boolean title;
		private boolean id;
		private boolean description;
		private boolean archived;
		private boolean user_permission;
		private Binding binding;
	}

	@Data
	public static class Container {
		private boolean id;
		private boolean title;
	}

	@Data
	public static class Binding {
		private boolean id;
		private boolean type;
		private Storage storage;
		private boolean meta;
	}

	@Data
	public static class Storage {
		private boolean title;
	}

	@Data
	public static class Tags {
		private boolean id;
		private boolean name;
	}

	@Data
	public static class Views {
		private boolean count;
	}

	@Data
	public static class SchemaProperties {
		private boolean id;
		private boolean spaceId;
		private boolean propertyId;
		private boolean name;
		private boolean type;
		private boolean code;
		private boolean format;
		private boolean options;
		private boolean _protected;
		private boolean hide;
		private boolean sort;
		private boolean version;
		private boolean versionAt;
		private boolean createdBy;
		private boolean updatedBy;
		private boolean createdAt;
		private boolean updatedAt;
		private boolean commands;
	}
}
