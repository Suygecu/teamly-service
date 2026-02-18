package service.teamly;


import lombok.Data;

@Data
public class TeamlyRequestTable {

	private Query query;

	@Data
	public static class Query {
		private Filter __filter;
		private boolean id;
		private boolean title;
		private Author author;
		private boolean created_at;
		private boolean updated_at;
		private boolean breadcrumbs;
		private Classifiers classifiers;
		private Tags tags;
		private boolean archived;
		private Container container;
		private MainArticle main_article;
		private SchemaProperties schemaProperties;
		private Content content;
		private boolean availableActions;
	}

	@Data
	public static class Filter {
		private String contentDatabaseId;
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
	public static class Classifiers {
		private boolean id;
		private boolean title;
	}

	@Data
	public static class Tags {
		private boolean id;
		private boolean name;
	}

	@Data
	public static class Container {
		private boolean id;
		private SchemaProperties schemaProperties;
	}

	@Data
	public static class MainArticle {
		private boolean id;
		private boolean anchored;
		private boolean is_hidden;
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
		private boolean _protected; // reserved keyword
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

	@Data
	public static class Content {
		private Article article;
		private boolean hasNested;
	}

	@Data
	public static class Article {
		private boolean id;
		private Properties properties;
	}

	@Data
	public static class Properties {
		private boolean properties;
	}

}
