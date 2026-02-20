package com.suygecu.teamly_service.dto;

import lombok.Data;

import java.util.List;

@Data
public class GroupPayload {
	private List<CommandRequest> commands;
}
