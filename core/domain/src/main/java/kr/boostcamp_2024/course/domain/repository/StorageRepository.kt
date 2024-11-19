package kr.boostcamp_2024.course.domain.repository

interface StorageRepository {
    suspend fun uploadImage(imageByteArray: ByteArray): Result<String>

    suspend fun deleteImage(imageUrl: String): Result<Unit>
}
