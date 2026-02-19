package com.release.jira_api_release.service

import com.release.jira_api_release.service.confluence.ConfluenceDownloadPageService
import com.release.jira_api_release.service.confluence.UpdateHtmlTableRelease
import com.release.jira_api_release.service.confluence.ConfluenceUploadPageService
import com.release.jira_api_release.service.jira.IssueProcessor
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Component
class ReleaseTableOperator(
    private val confluenceDownloadPageService: ConfluenceDownloadPageService,
    private val confluenceUploadPageService: ConfluenceUploadPageService,
    private val buildEpicReport: IssueProcessor,
    private val htmlTableUpdater: UpdateHtmlTableRelease,
    @Value("\${confluence.html.directory}")
    private val htmlDirectory: String
) {
    private val releaseOperationPoll: ExecutorService = Executors.newSingleThreadExecutor()

    fun updateReleaseTable(projectId: String, pageId: String) {
        releaseOperationPoll.submit {
            try {
                val pathPageDir: Path = Paths.get(htmlDirectory)
                val inPutFile = pathPageDir.resolve("${pageId}_release.html")
                val outPutFile = pathPageDir.resolve("${pageId}_release_updated.html")

                Files.createDirectories(pathPageDir)


                try {
                    val pageIdLong = pageId.toLongOrNull()
                        ?: throw IllegalArgumentException("Неверный формат pageId: $pageId. Ожидается числовое значение.")

                    confluenceDownloadPageService.loadHtml(pageIdLong)
                } catch (e: NumberFormatException) {
                    throw IllegalArgumentException("Неверный формат pageId: $pageId. Ожидается числовое значение.", e)
                }


                if (!Files.exists(inPutFile)) {
                    throw RuntimeException("HTML-файл не был загружен по пути: $inPutFile")
                }

                val tableData = buildEpicReport.buildEpicReport(projectId, true)


                htmlTableUpdater.updateReleaseTable(pageId, tableData)

                Thread.sleep(1000)

                if (!Files.exists(outPutFile)) {
                    throw RuntimeException("Обновленный HTML-файл не найден: $outPutFile")
                }

                confluenceUploadPageService.confluenceUploadPageService(pageId, outPutFile.toString())

            } catch (e: Exception) {
                throw RuntimeException("Ошибка при обновлении таблицы релиза: ${e.message}", e)
            }


        }

    }

}