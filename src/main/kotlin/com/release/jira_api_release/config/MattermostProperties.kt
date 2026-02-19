package com.release.jira_api_release.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "mattermost")
    open class MattermostProperties {
        lateinit var hostname: String
        lateinit var authToken: String
        lateinit var wsToken: String
        lateinit var channel: String
}