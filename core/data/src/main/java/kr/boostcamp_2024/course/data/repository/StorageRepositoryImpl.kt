package kr.boostcamp_2024.course.data.repository

import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import kr.boostcamp_2024.course.domain.repository.StorageRepository
import java.util.UUID
import javax.inject.Inject

class StorageRepositoryImpl @Inject constructor(
    storage: FirebaseStorage,
) : StorageRepository {
    private val storageRef = storage.reference

    override suspend fun uploadImage(imageByteArray: ByteArray): Result<String> = runCatching {
        val uuid = UUID.randomUUID().toString()
        val fileRef = storageRef.child(uuid)

        fileRef.putBytes(imageByteArray).await()

        fileRef.downloadUrl.await().toString()
    }

    override suspend fun deleteImage(imageUrl: String): Result<Unit> = runCatching {
        val filePath = extractPathFromUrl(imageUrl)
        val fileRef = storageRef.child(filePath)
        fileRef.delete().await()
    }

    override suspend fun deleteImages(imageUrls: List<String>): Result<Unit> = runCatching {
        imageUrls.forEach { imageUrl ->
            val filePath = extractPathFromUrl(imageUrl)
            val fileRef = storageRef.child(filePath)
            fileRef.delete().await()
        }
    }

    private fun extractPathFromUrl(imageUrl: String): String {
        val startIndex = imageUrl.indexOf("o/") + 2
        val endIndex = imageUrl.indexOf("?")
        return imageUrl.substring(startIndex, endIndex)
    }
}
