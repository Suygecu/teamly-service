package com.suygecu.teamly_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;


@Data
public class TeamlyRequestSpace {
	@JsonProperty("query")
	private Query query;



	@Data
	public static class Query {
		private Filter __filter;
		private ShortcutLinks shortcut_links;
		private Boolean id;
		private Boolean title;
		private Boolean description;
		private Boolean pinned_at;
		private Boolean archived;
		private Boolean keeping_type;
		private Boolean user_permission;
		private Likes likes;
		private Avatar avatar;
		private MainArticle main_article;
		private Binding binding;

	}
	@Data
	public static class Filter {
		private String id;


	}
	@Data
	public static class ShortcutLinks {
		private Boolean id;
		private Boolean title;
		private Boolean link;
		private Boolean link_type;
		private List<Map<String, String>> __sort;
		private Pagination __pagination;

	}
	@Data
	public static class Pagination {
		private Integer page;
		private Integer per_page;

	}
	@Data
	public static class Likes {
		private Boolean count;

	}
	@Data
	public static class Avatar {
		private Boolean path;

	}
	@Data
	public static class MainArticle {
		private Boolean id;
		private Boolean icon_color;

	}
	@Data
	public static class Binding {
		private Boolean id;
		private Boolean type;
		private Storage storage;
		private Boolean meta;

	}
	@Data
	public static class Storage {
		private Boolean title;


	}
}
