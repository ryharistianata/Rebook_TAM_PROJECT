package com.kelompok.rebook.data

enum class BookStatus {
    AVAILABLE,
    BORROWED,
    DONATED
}

data class Book(
    val id: String,
    val title: String,
    val author: String,
    val description: String,
    val coverUrl: String?,
    val isbn: String? = null,
    val publishYear: String? = null,
    val subject: String? = null,
    val status: BookStatus = BookStatus.AVAILABLE,
    val ownerName: String,
    val ownerWhatsApp: String,
    val uploadedAt: Long = System.currentTimeMillis()
)
data class OpenLibrarySearchResponse(
    val numFound: Int,
    val docs: List<OpenLibraryDoc>
)

data class OpenLibraryDoc(
    val key: String,
    val title: String,
    val author_name: List<String>?,
    val first_publish_year: Int?,
    val isbn: List<String>?,
    val cover_i: Int?,
    val subject: List<String>?,
    val first_sentence: Map<String, String>?
) {
    val coverUrl: String?
        get() = cover_i?.let {
            "https://covers.openlibrary.org/b/id/$it-M.jpg"
        }

    val primaryAuthor: String
        get() = author_name?.firstOrNull() ?: "Penulis Tidak Diketahui"

    val shortDescription: String
        get() = first_sentence?.get("value") ?: "Deskripsi tidak tersedia."
}

sealed class ApiResult<out T> {
    data class  Success<T>(val data: T) : ApiResult<T>()
    data class  Error(val message: String) : ApiResult<Nothing>()
    object      Loading : ApiResult<Nothing>()
}