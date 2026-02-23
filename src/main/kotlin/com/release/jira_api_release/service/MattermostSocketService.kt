package com.release.jira_api_release.service

import com.release.jira_api_release.bot.dispather.MattermostEventDispatcher
import com.release.jira_api_release.bot.handlers.command.CommandDispatcher
import com.release.jira_api_release.bot.handlers.command.ShutDownCommandHandler
import com.release.jira_api_release.bot.handlers.command.SynbuildCommandHandler
import com.release.jira_api_release.bot.handlers.dialog.ReleaseDialogHandler
import com.release.jira_api_release.config.MattermostProperties
import jakarta.annotation.PostConstruct
import net.exbo.mattermost.client.MattermostClient
import org.springframework.context.ConfigurableApplicationContext

import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicBoolean

@Component
class MattermostSocketService(
    private val releaseDialogHandler: ReleaseDialogHandler,
    private val props: MattermostProperties,
    private val client: MattermostClient,
    private val updateSummaryState: UpdateSummaryState,
    private val ctx : ConfigurableApplicationContext
) {

    private val commandDispatcher = CommandDispatcher(
        listOf(
            ShutDownCommandHandler(),
            SynbuildCommandHandler()
        )
    )

    private var started = AtomicBoolean(false)
    private val dispatcher = MattermostEventDispatcher(
        client,
        commandDispatcher,
        releaseDialogHandler,
        updateSummaryState,
    )

    @PostConstruct
    fun startWebSocket() {
        if (!started.compareAndSet(false, true)) return

        client.start {
            dispatcher.handler(this)
            println(this.javaClass.simpleName + " connected")
        }
    }
}
















