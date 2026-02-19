package com.release.jira_api_release.config

import net.exbo.mattermost.client.MattermostClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class MattermostConfig {

    @Bean
    open fun MattermostProperties.mattermostClient(): MattermostClient {
        return MattermostClient(
            hostname,
            authToken,
            wsToken
        )
    }
}