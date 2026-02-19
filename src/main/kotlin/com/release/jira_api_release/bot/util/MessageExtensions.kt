package com.release.jira_api_release.bot.util

import net.exbo.mattermost.client.MattermostClient
import net.exbo.mattermost.client.ws.NewPostMessage

fun NewPostMessage.isFromBot(client: MattermostClient): Boolean {
    return this.post.user_id == client.wsToken
}