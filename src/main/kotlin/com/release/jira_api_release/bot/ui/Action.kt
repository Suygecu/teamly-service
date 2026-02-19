package com.release.jira_api_release.bot.ui

import net.exbo.mattermost.client.data.MattermostAction
import net.exbo.mattermost.client.data.MattermostActionType
import net.exbo.mattermost.client.data.MattermostIntegration

object Action {

    fun createReleaseTableButton(): MattermostAction {
        return MattermostAction(
            type = MattermostActionType.BUTTON,
            name = "Создать релизную таблицу test",
            style = "primary",
            integration = MattermostIntegration(
                context = mapOf(
                    "open_dialog" to "some_dialog"
                )
            )
        )

    }
    fun  createArtistUpdateButton(): MattermostAction {
        return MattermostAction(
            type = MattermostActionType.BUTTON,
            name = "Обновить таблицу отчета",
            style = "primary",
            integration = MattermostIntegration(
                context = mapOf(
                    "update_artists" to "update_artists_action"
                )
            )
        )
    }
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