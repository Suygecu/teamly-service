package teamly;

import lombok.Data;

@Data
public class NestedContentRequest {
	private Query query;

	@Data
	public static class Query {
		private Pagination __pagination;
		private Filter __filter;
		private Article article;
		private Boolean hasNested;
	}

	@Data
	public static class Pagination {
		private int page;
		private int per_page;
	}

	@Data
	public static class Filter {
		private String parentId;
	}

	@Data
	public static class Article {
		private boolean id;
		private Properties properties;

		@Data
		public static class Properties {
			private boolean properties;
		}
	}
}
