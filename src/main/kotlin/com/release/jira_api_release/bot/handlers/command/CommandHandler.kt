package com.release.jira_api_release.bot.handlers.command

import net.exbo.mattermost.client.MattermostClient
import net.exbo.mattermost.client.ws.NewPostMessage

interface CommandHandler {
    fun canHandle(message: NewPostMessage): Boolean
    fun handle(message: NewPostMessage, client: MattermostClient)
}