package service.teamly;

import lombok.Data;

@Data
public class PropertyUpdatePayload {
	private EntityRef entity;
	private Operation operation;

	@Data
	public static class Operation {
		private String codeProperty;    // код свойства (например, title, OJld)
		private String method;  // update, add
		private Object value;   // строка / объект / массив
	}
}
