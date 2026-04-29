package com.suygecu.teamly_service.dto.outline

data class ProsemirrorUpdateResponse(
    val data: Data
) {
    data class Data(
        val id: String
    )
}