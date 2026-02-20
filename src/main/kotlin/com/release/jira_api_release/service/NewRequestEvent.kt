package com.release.jira_api_release.service

data class NewRequestEvent(
    val title: String,
    val authorTag: String? = null,
    val requestId: String? = null,
)