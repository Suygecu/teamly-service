package teamly;

import lombok.Data;

import java.util.List;

@Data
public class GroupPayload {
	private List<CommandRequest> commands;
}
