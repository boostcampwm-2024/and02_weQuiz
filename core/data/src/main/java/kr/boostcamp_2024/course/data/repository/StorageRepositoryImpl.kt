package kr.boostcamp_2024.course.data.repository

import android.util.Log
import androidx.core.net.toUri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import kr.boostcamp_2024.course.domain.repository.StorageRepository
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

class StorageRepositoryImpl @Inject constructor(
    storage: FirebaseStorage,
) : StorageRepository {
    private val storageRef = storage.reference

    override suspend fun uploadImage(imageUri: String): Result<String> = runCatching {
        val startTime = LocalDateTime.now()

        val uuid = UUID.randomUUID().toString()
        val fileRef = storageRef.child(uuid)

        fileRef.putFile(imageUri.toUri()).await()

        val endTime = LocalDateTime.now()
        Log.d("startTime", startTime.toString())
        Log.d("endTime", endTime.toString())

        fileRef.downloadUrl.await().toString()
    }
}
