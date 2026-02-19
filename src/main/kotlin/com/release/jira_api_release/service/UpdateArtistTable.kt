package com.release.jira_api_release.service

import com.release.jira_api_release.service.confluence.ConfluenceDownloadPageService
import com.release.jira_api_release.service.confluence.ConfluencePage
import com.release.jira_api_release.service.confluence.ConfluenceUploadPageService
import com.release.jira_api_release.service.confluence.UpdateHtmlArtistTableReport
import com.release.jira_api_release.service.jira.IssueProcessor
import com.release.jira_api_release.service.jira.request.JiraRequestTypeContent
import io.ktor.client.utils.EmptyContent.contentType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.Collections
import java.util.concurrent.Executors
import java.util.concurrent.Semaphore

@Component
class UpdateArtistTable(
    private val confluenceDownloadPageService: ConfluenceDownloadPageService,
    private val confluenceUploadPageService: ConfluenceUploadPageService,
    private val htmlTableUpdater: UpdateHtmlArtistTableReport,
    private val jiraRequestTypeContent: JiraRequestTypeContent,
    private val issueProcessor: IssueProcessor,
    @Value("\${confluence.html.directory}")
    private val htmlDirectory: String,
    @Autowired
    private val updateSummaryState: UpdateSummaryState
) {
    fun updateArtistTable(pageId: Long, contentType: String) {



        println("🚀 Обновление таблицы Отчета для страницы: $pageId, тип: $contentType")
        try {
            val pathPageDir: Path = Paths.get(htmlDirectory).toAbsolutePath()
            Files.createDirectories(pathPageDir)

            val inputFile = pathPageDir.resolve("${pageId}_${contentType}.html")
            val outputFile = pathPageDir.resolve("${pageId}_${contentType}_updated.html")

            println("🚀 Загружаем страницу из Confluence...")
            loadHtmlWithRetry(pageId, inputFile)


            if (!Files.exists(inputFile)) {
                throw RuntimeException(" HTML-файл не был загружен по пути: $inputFile")
            }

            println("🚀 Получаем список задач из Jira...")
            val jqlQuery = contentType
            println("🚀 Получаем задачи для типа = $contentType")
            val issues = jiraRequestTypeContent.getIssueContent(jqlQuery, jiraRequestTypeContent)
            println("📦 Получено ${issues.size} задач")

            val result = issueProcessor.buildArtistReport(issues)

            if (result.isEmpty()) {
                throw RuntimeException("Нет данных для обновления таблицы по типу $contentType")
            }





            println("Обновляем таблицу артистов...")
            htmlTableUpdater.updateArtistTableReport("${pageId}_${contentType}", result)

            waitForFile(outputFile)

            if (!Files.exists(outputFile)) {
                val workingDirPath = Paths.get("").toAbsolutePath()
                val alternativePath = workingDirPath.resolve("build/tmp/confluence/${pageId}_updated.html")

                if (Files.exists(alternativePath)) {
                    println("✅ Найден файл в альтернативном расположении: $alternativePath")
                    Files.copy(alternativePath, outputFile)
                } else {
                    throw RuntimeException("Обновленный HTML-файл не найден: $outputFile и $alternativePath")
                }
            }
            println("🚀 Загружаем обновленную таблицу обратно в Confluence...")
                confluenceUploadPageService.confluenceUploadPageService(pageId.toString(), outputFile.toString())



            println("Таблица артистов успешно обновлена!")

        } catch (e: Exception) {
            println("Ошибка при обновлении таблицы артистов: ${e.message}")
            e.printStackTrace()
            throw e
        }
    }

    fun updateAllTablesInParallel() {

        val updatedPages = Collections.synchronizedList(mutableListOf<String>())
        val executor = Executors.newFixedThreadPool(7)
        val semaphore = Semaphore(7)
        val futures = ConfluencePage.values().map { page ->
            executor.submit {
                semaphore.acquire()
                try {
                    val contentType = page.contentTypeName


                    println("[$contentType] Старт потока")
                    updateArtistTable(page.pageId, contentType)
                    println("[$contentType] Завершено")
                    val pageId = page.pageId
                    val url = "https://wiki.xexbo.ru/pages/viewpage.action?pageId=$pageId"
                    updatedPages.add("[$contentType] страница обновлена: $url")

                } catch (e: Exception) {
                    println(" [$contentType] Ошибка: ${e.message}")
                    e.printStackTrace()
                }finally {
                    semaphore.release()
                }
            }
        }

        futures.forEach { it.get() }
        executor.shutdown()

         updateSummaryState.summaryPage = updatedPages.joinToString(separator = "\n")

    }
    private fun waitForFile(filePath: Path, maxWaitMillis: Long = 10000, checkIntervalMillis: Long = 200): Boolean {
        val startTime = System.currentTimeMillis()
        while (System.currentTimeMillis() - startTime < maxWaitMillis) {
            if (Files.exists(filePath)) {
                println("Найден файл: $filePath")
                return true
            }
            Thread.sleep(checkIntervalMillis)
        }
        println("Файл не появился за ${maxWaitMillis} мс: $filePath")
        return false
    }

    private fun loadHtmlWithRetry(pageId: Long, filePath: Path, timeoutMillis: Long = 500, retryCount: Int = 3, retryDelayMillis: Long = 5000) {
        var lastException: Exception? = null

        repeat(retryCount) { attempt ->
            try {
                println("Попытка загрузки #${attempt + 1} страницы $pageId")
                confluenceDownloadPageService.loadHtml(pageId)
                val appeared = waitForFile(filePath, 10000)
                if (!appeared) throw RuntimeException("Файл не найден после загрузки: $filePath")
                return
            } catch (e: Exception) {
                lastException = e
                println("Неудачная попытка #${attempt + 1}: ${e.message}")
                if (attempt < retryCount - 1) {
                    Thread.sleep(retryDelayMillis)
                }
            }
        }

        throw RuntimeException("Не удалось загрузить HTML-страницу после $retryCount попыток: $filePath", lastException)
    }

}