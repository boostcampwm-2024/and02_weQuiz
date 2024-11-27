package kr.boostcamp_2024.course.data.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kr.boostcamp_2024.course.data.model.QuizDTO
import kr.boostcamp_2024.course.data.model.RealTimeQuizDTO
import kr.boostcamp_2024.course.domain.enum.QuizType.Companion.getQuizTypeFromValue
import kr.boostcamp_2024.course.domain.enum.QuizType.General
import kr.boostcamp_2024.course.domain.enum.QuizType.RealTime
import kr.boostcamp_2024.course.domain.model.BaseQuiz
import kr.boostcamp_2024.course.domain.model.QuizCreationInfo
import kr.boostcamp_2024.course.domain.model.RealTimeQuiz
import kr.boostcamp_2024.course.domain.repository.QuizRepository
import javax.inject.Inject

class QuizRepositoryImpl @Inject constructor(
    firestore: FirebaseFirestore,
) : QuizRepository {
    private val quizCollectionRef = firestore.collection("Quiz")

    override suspend fun addQuestionToQuiz(quizId: String, questionId: String): Result<Unit> =
        runCatching {
            val document = quizCollectionRef.document(quizId)
            document.update("questions", FieldValue.arrayUnion(questionId)).await()
        }

    override suspend fun createQuiz(quizCreateInfo: QuizCreationInfo, ownerId: String?): Result<String> =
        runCatching {
            when (ownerId) {
                null -> {
                    val newQuiz = QuizDTO(
                        title = quizCreateInfo.quizTitle,
                        description = quizCreateInfo.quizDescription,
                        startTime = quizCreateInfo.quizDate,
                        solveTime = quizCreateInfo.quizSolveTime,
                        questions = emptyList(),
                        userOmrs = emptyList(),
                        quizImageUrl = quizCreateInfo.quizImageUrl,
                        type = GENERAL_QUIZ,
                    )
                    quizCollectionRef.add(newQuiz).await().id
                }

                else -> {
                    val newRealTimeQuiz = RealTimeQuizDTO(
                        title = quizCreateInfo.quizTitle,
                        description = quizCreateInfo.quizDescription,
                        questions = emptyList(),
                        userOmrs = emptyList(),
                        currentQuestion = 0,
                        ownerId = ownerId,
                        isStarted = false,
                        isFinished = false,
                        waitingUsers = emptyList(),
                        quizImageUrl = quizCreateInfo.quizImageUrl,
                        type = REAL_TIME_QUIZ,
                    )
                    quizCollectionRef.add(newRealTimeQuiz).await().id
                }
            }
        }

    override suspend fun getQuiz(quizId: String): Result<BaseQuiz> =
        runCatching {
            val document = quizCollectionRef.document(quizId).get().await()
            val quizType = document.get("type").toString()
            val response = when (getQuizTypeFromValue(quizType)) {
                RealTime -> document.toObject(RealTimeQuizDTO::class.java)?.toVO(quizId)
                General -> document.toObject(QuizDTO::class.java)?.toVO(quizId)
            }
            requireNotNull(response)
        }

    override suspend fun getQuizList(quizIdList: List<String>): Result<List<BaseQuiz>> =
        runCatching {
            quizIdList.map { quizId ->
                val document = quizCollectionRef.document(quizId).get().await()
                val quizType = document.get("type").toString()
                val response = when (getQuizTypeFromValue(quizType)) {
                    RealTime -> document.toObject(RealTimeQuizDTO::class.java)?.toVO(quizId)
                    General -> document.toObject(QuizDTO::class.java)?.toVO(quizId)
                }
                requireNotNull(response)
            }
        }

    override suspend fun addUserOmrToQuiz(quizId: String, userOmrId: String): Result<Unit> =
        runCatching {
            quizCollectionRef.document(quizId)
                .update("user_omrs", FieldValue.arrayUnion(userOmrId))
                .await()
        }

    override suspend fun editQuiz(quizId: String, quizCreateInfo: QuizCreationInfo): Result<Unit> =
        runCatching {
            val updatedData = mapOf(
                "title" to quizCreateInfo.quizTitle,
                "description" to quizCreateInfo.quizDescription,
                "start_time" to quizCreateInfo.quizDate,
                "solve_time" to quizCreateInfo.quizSolveTime,
                "quiz_image_url" to quizCreateInfo.quizImageUrl,
            )
            quizCollectionRef.document(quizId)
                .update(updatedData)
                .await()
        }

    override suspend fun deleteQuiz(quizId: String): Result<Unit> =
        runCatching {
            quizCollectionRef.document(quizId).delete().await()
        }

    override suspend fun deleteQuizzes(quizzes: List<String>): Result<Unit> =
        runCatching {
            quizzes.forEach { quizId ->
                quizCollectionRef.document(quizId).delete().await()
            }
        }

    override suspend fun startRealTimeQuiz(quizId: String): Result<Unit> =
        runCatching {
            quizCollectionRef.document(quizId)
                .update("is_started", true)
                .await()
        }

    override suspend fun waitingRealTimeQuiz(quizId: String, waiting: Boolean, userId: String): Result<Unit> =
        runCatching {
            when (waiting) {
                true -> {
                    quizCollectionRef.document(quizId)
                        .update("waiting_users", FieldValue.arrayUnion(userId))
                        .await()
                }

                false -> {
                    quizCollectionRef.document(quizId)
                        .update("waiting_users", FieldValue.arrayRemove(userId))
                        .await()
                }
            }
        }

    override fun observeQuiz(quizId: String): Flow<BaseQuiz> = callbackFlow<BaseQuiz> {
        val listener = quizCollectionRef.document(quizId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                }

                if (snapshot?.exists() == true) {
                    try {
                        val quizType = snapshot.get("type").toString()
                        val response = when (getQuizTypeFromValue(quizType)) {
                            RealTime -> snapshot.toObject(RealTimeQuizDTO::class.java)?.toVO(quizId)
                            General -> snapshot.toObject(QuizDTO::class.java)?.toVO(quizId)
                        }
                        val quiz =
                            requireNotNull(response) { "Quiz response is null for quizId: $quizId" }
                        trySend(quiz)
                    } catch (exception: Exception) {
                        close(exception)
                    }
                } else {
                    close(Exception("Quiz not found"))
                }
            }
        awaitClose {
            listener.remove()
        }
    }

    override fun observeRealTimeQuiz(quizId: String): Flow<Result<RealTimeQuiz>> = callbackFlow {
        val quizDocument = quizCollectionRef.document(quizId)
        val listener = quizDocument.addSnapshotListener { documentSnapshot, error ->
            runCatching {
                if (error != null) {
                    close(error)
                }

                if (documentSnapshot?.exists() == true) {
                    val response = documentSnapshot.toObject(RealTimeQuizDTO::class.java)?.toVO(quizId)
                    trySend(Result.success(requireNotNull(response)))
                } else {
                    throw Exception("문서가 존재하지 않습니다")
                }
            }.onFailure { exception ->
                trySend(Result.failure(exception))
            }
        }

        awaitClose {
            listener.remove()
        }
    }

    override suspend fun setQuizFinished(quizId: String): Result<Unit> =
        runCatching {
            quizCollectionRef.document(quizId)
                .update("is_finished", true)
                .await()
        }

    override suspend fun updateQuizCurrentQuestion(quizId: String, currentQuestion: Int): Result<Unit> =
        runCatching {
            quizCollectionRef.document(quizId)
                .update("current_question", currentQuestion)
                .await()
        }

    companion object {
        private const val GENERAL_QUIZ = "general"
        private const val REAL_TIME_QUIZ = "realTime"
    }
}
