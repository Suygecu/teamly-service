package teamly;

import lombok.Data;

@Data
public class EntityRef {
	private String spaceId;
	private String articleId;
	private String parentId;
}
