package com.suygecu.teamly_service.dto.outline

data class DocumenOutlineUpdateRequest(
    val `data`: DataX,
    val policies: List<PolicyX>
)