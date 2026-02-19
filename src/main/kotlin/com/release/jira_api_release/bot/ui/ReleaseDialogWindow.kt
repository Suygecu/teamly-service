package com.release.jira_api_release.bot.ui

import net.exbo.mattermost.client.data.MattermostDialog
import net.exbo.mattermost.client.data.MattermostDialogElement
import net.exbo.mattermost.client.ws.OpenDialogMessage
import net.exbo.mattermost.client.ws.OpenDialogRequest

object ReleaseDialogWindow {

    fun createReleaseDialog(triggerId: String, channelId: String): OpenDialogMessage {
        return OpenDialogMessage(
            OpenDialogRequest(
                dialog = MattermostDialog(
                    state = """{ "channelId": "$channelId" }""",
                    title = "",
                    submit_label = "Готово!",
                    notify_on_cancel = false,
                    elements = listOf(
                        MattermostDialogElement(
                            "projectID", "ID project"
                        ), MattermostDialogElement(
                            "pageID", "PageID confluence"
                        )

                    )

                ),

                trigger_id = triggerId
            )
        )
    }
}