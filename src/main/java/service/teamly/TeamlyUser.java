package service.teamly;

import lombok.Data;

@Data
public class TeamlyUser {
	private String id;
	private String fullName;
	private String name;
	private String surname;
	private String avatarPath;
	private String externalId;
}