package com.suygecu.teamly_service.config;

import net.exbo.mattermost.client.MattermostClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MattermostClientConfig {

	@Bean
	public MattermostClient mattermostClient(
			@Value("${mattermost.hostname}") String hostname,
			@Value("${mattermost.auth-token}") String authToken,
			@Value("${mattermost.ws-token}") String wsToken
	) {
		return new MattermostClient(hostname, authToken, wsToken);
	}
}
