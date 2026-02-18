package service.teamly;

import lombok.Data;

@Data
public class PropertyOperation {
	private String method;
	private String code;
	private Object value;
}
