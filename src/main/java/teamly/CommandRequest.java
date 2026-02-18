package teamly;

import lombok.Data;

@Data
public class CommandRequest {
	private String code;
	private Object payload;
}
