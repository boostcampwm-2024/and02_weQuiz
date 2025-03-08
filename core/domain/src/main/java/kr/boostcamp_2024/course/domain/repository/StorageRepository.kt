package kr.boostcamp_2024.course.domain.repository

interface StorageRepository {
    suspend fun uploadImage(imageByteArray: ByteArray): String

    suspend fun deleteImage(imageUrl: String)

    suspend fun deleteImages(imageUrls: List<String>)
}
