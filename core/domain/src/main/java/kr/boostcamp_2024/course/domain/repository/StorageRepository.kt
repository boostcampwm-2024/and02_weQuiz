package kr.boostcamp_2024.course.domain.repository

interface StorageRepository {
    suspend fun uploadImage(imageUri: String): Result<String>
}
