package com.release.jira_api_release.bot.dispather

import com.release.jira_api_release.bot.ui.ReleaseDialogWindow
import com.release.jira_api_release.bot.handlers.command.CommandDispatcher
import com.release.jira_api_release.bot.handlers.dialog.ReleaseDialogHandler
import com.release.jira_api_release.bot.util.isFromBot
import com.release.jira_api_release.service.UpdateSummaryState
import com.suygecu.teamly_service.teamly.TeamlyQuerySyncBuildService
import net.exbo.mattermost.client.MattermostClient
import net.exbo.mattermost.client.data.MattermostPost

import net.exbo.mattermost.client.ws.*
import kotlin.system.exitProcess

class MattermostEventDispatcher(
    private val client: MattermostClient,
    private val commandDispatcher: CommandDispatcher,
    private val releaseDialogHandler: ReleaseDialogHandler,
    private val updateSummaryState: UpdateSummaryState,
    private val teamlyQuerySyncBuildService: TeamlyQuerySyncBuildService
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
                    event.action.context["sdohny"] == "sdohny_success" ->{

                        val chaneId = event.action.channel_id
                        client.sendMessage(
                            CreatePostMessage(
                                MattermostPost(
                                    channel_id = chaneId,
                                    message = "Ya sdohla..."
                                )
                            )

                        )
                        exitProcess(0)


                    }

                    event.action.context["open_dialog"] == "some_dialog" -> {
                        client.sendMessage(
                            ReleaseDialogWindow.createReleaseDialog(
                                event.action.trigger_id,
                                event.action.channel_id
                            )
                        )
                    }

                    event.action.context["syncbuild"] == "syncbuild_accepted" -> {
                        val channelId = event.action.channel_id

                        client.sendMessage(
                            CreatePostMessage(
                                MattermostPost(
                                    channel_id = channelId,
                                    message = "---\n:hourglass_flowing_sand: Проверяю статусы SyncBuild..."
                                )
                            )
                        )

                        try {
                            val result = teamlyQuerySyncBuildService.requestSyncBuildTable()

                            client.sendMessage(
                                CreatePostMessage(
                                    MattermostPost(
                                        channel_id = channelId,
                                        message = if (result.isBlank()) {
                                            "---\n✅ Команда: syncbuild general -chunkdist=16 -nostart -nolocs=" + result
                                        } else {
                                            "---\n✅  syncbuild general -chunkdist=16 -nostart -nolocs=$result"
                                        }
                                    )
                                )
                            )
                        } catch (e: Exception) {
                            client.sendMessage(
                                CreatePostMessage(
                                    MattermostPost(
                                        channel_id = channelId,
                                        message = "❌ Ошибка при проверке SyncBuild: ${e.message}"
                                    )
                                )
                            )
                        }
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