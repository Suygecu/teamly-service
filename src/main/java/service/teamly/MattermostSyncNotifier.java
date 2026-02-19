package service.teamly;

import net.exbo.mattermost.client.MattermostClient;
import net.exbo.mattermost.client.data.MattermostPost;
import net.exbo.mattermost.client.ws.CreatePostMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MattermostSyncNotifier {

	private final MattermostClient mattermostClient;

	@Value("${mattermost.channel:}")
	private String channelId;

	public MattermostSyncNotifier(MattermostClient mattermostClient) {
		this.mattermostClient = mattermostClient;
	}

	public void notifyNewSyncRequest(String message) {
		if (channelId == null || channelId.isBlank()) {
			System.out.println("[MATTERMOST] Пропущено уведомление: не настроен mattermost.channel");
			return;
		}

		try {
			MattermostPost post = new MattermostPost(
					channelId,
					message,
					null,
					null,
					null,
					null
			);
			mattermostClient.sendMessage(new CreatePostMessage(post));
		} catch (Exception e) {
			System.err.println("[MATTERMOST] Ошибка отправки уведомления: " + e.getMessage());
		}
	}
}
