package com.release.jira_api_release.bot.handlers.command

import com.release.jira_api_release.bot.ui.CreateArtistPost
import net.exbo.mattermost.client.MattermostClient
import net.exbo.mattermost.client.ws.NewPostMessage

class ArtistsCommandHandler : CommandHandler {
    override fun canHandle(message: NewPostMessage): Boolean {
        return message.post.message?.trim()?.lowercase() == "artists"
    }

    override fun handle(message: NewPostMessage, client: MattermostClient) {
        client.sendMessage(
            CreateArtistPost.createArtistPost(message.post.channel_id!!)
        )
    }
}