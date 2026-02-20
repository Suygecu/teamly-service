package com.suygecu.teamly_service.dto;

import lombok.Data;

@Data
public class CommandRequest {
	private String code;
	private Object payload;
}
