package com.release.jira_api_release.bot.ui

import net.exbo.mattermost.client.data.MattermostAction
import net.exbo.mattermost.client.data.MattermostActionType
import net.exbo.mattermost.client.data.MattermostIntegration

object Action {

    fun  shutDown(): MattermostAction {
        return MattermostAction(
            type = MattermostActionType.BUTTON,
            name = "Нажми, что бы свершить убийство",
            style = "primary",
            integration = MattermostIntegration(
                context = mapOf(
                    "sdohny" to "sdohny_success"
                )
            )
        )
    }

}