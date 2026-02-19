package com.release.jira_api_release.bot.handlers.command

import net.exbo.mattermost.client.MattermostClient


import net.exbo.mattermost.client.ws.NewPostMessage
import org.springframework.context.annotation.Bean

class CommandDispatcher(
    private val handlers: List<CommandHandler>
){
    fun dispatch(message: NewPostMessage, client: MattermostClient) {
        handlers.firstOrNull { it.canHandle(message) }?.handle(message, client)

    }
    @Bean
    fun commandDispatcher(
        shutDownCommandHandler: ShutDownCommandHandler
    ): CommandDispatcher {
        return CommandDispatcher(listOf(shutDownCommandHandler))
    }
}

