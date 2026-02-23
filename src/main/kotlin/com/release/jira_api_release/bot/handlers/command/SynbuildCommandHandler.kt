package com.release.jira_api_release.bot.handlers.command

import com.release.jira_api_release.bot.ui.CreateShutDownPost
import com.release.jira_api_release.bot.ui.CreateSyncbuildPost
import net.exbo.mattermost.client.MattermostClient
import net.exbo.mattermost.client.ws.NewPostMessage

class SynbuildCommandHandler : CommandHandler {
    override fun canHandle(message: NewPostMessage): Boolean {
        val text = message.post.message?.trim()?.lowercase() ?: return false
        return text.contains("syncbuild")
    }

    override fun handle(
        message: NewPostMessage,
        client: MattermostClient
    ) {
        client.sendMessage(
            CreateSyncbuildPost.createSyncbuildPost(message.post.channel_id!!)
        )
    }


}