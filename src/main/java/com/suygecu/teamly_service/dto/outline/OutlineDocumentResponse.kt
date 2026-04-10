import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class OutlineDocumentResponse(
    val data: OutlineDocumentData,
    val policies: List<OutlinePolicy>? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class OutlineDocumentData(
    val id: String,
    val collectionId: String? = null,
    val title: String? = null,
    val text: String? = null,
    val emoji: String? = null,
    val fullWidth: Boolean? = null,
    val templateId: String? = null,
    val parentDocumentId: String? = null,
    val urlId: String? = null,
    val archivedAt: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val publishedAt: String? = null,
    val deletedAt: Any? = null,
    val revision: Int? = null,
    val pinned: Boolean? = null,
    val dataAttributes: List<OutlineDataAttribute>? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class OutlineDataAttribute(
    val dataAttributeId: String? = null,
    val updatedAt: String? = null,
    val value: String? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class OutlinePolicy(
    val id: String? = null,
    val abilities: OutlineAbilities? = null
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class OutlineAbilities(
    val read: Boolean? = null,
    val update: Boolean? = null,
    val delete: Boolean? = null
)