package com.release.jira_api_release.bot.dispather

import com.release.jira_api_release.bot.ui.ReleaseDialogWindow
import com.release.jira_api_release.bot.handlers.command.CommandDispatcher
import com.release.jira_api_release.bot.util.isFromBot
import net.exbo.mattermost.client.MattermostClient

import net.exbo.mattermost.client.ws.*

class MattermostEventDispatcher(
    private val client: MattermostClient,
    private val commandDispatcher: CommandDispatcher,

) {

    fun handler(event: WSMessage) {
        when (event) {
            is AuthenticatedMessage -> {
                println("✅ Success auth!")
            }

            is NewPostMessage -> {
                if (!event.isFromBot(client)) {
                    commandDispatcher.dispatch(event, client)
                }
            }

            is ActionPerformedMessage -> {
                when {

                    event.action.context["open_dialog"] == "some_dialog" -> {
                        client.sendMessage(
                            ReleaseDialogWindow.createReleaseDialog(
                                event.action.trigger_id,
                                event.action.channel_id
                            )
                        )
                    }


                }
            }

            is DialogSubmissionMessage -> {


            }

            else -> {
                println("❓ Unknown message: $this")
            }
        }
    }
}