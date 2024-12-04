package kr.boostcamp_2024.course.data.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kr.boostcamp_2024.course.data.model.BlankQuestionDTO
import kr.boostcamp_2024.course.data.model.ChoiceQuestionDTO
import kr.boostcamp_2024.course.data.model.toDTO
import kr.boostcamp_2024.course.domain.enum.QuestionType
import kr.boostcamp_2024.course.domain.enum.toQuestionType
import kr.boostcamp_2024.course.domain.model.BlankQuestionCreationInfo
import kr.boostcamp_2024.course.domain.model.ChoiceQuestionCreationInfo
import kr.boostcamp_2024.course.domain.model.Question
import kr.boostcamp_2024.course.domain.repository.QuestionRepository
import javax.inject.Inject

class QuestionRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : QuestionRepository {
    private val questionCollectionRef = firestore.collection("Question")

    override suspend fun getQuestions(questionIds: List<String>): Result<List<Question>> =
        runCatching {
            questionIds.map { questionId ->
                val document = questionCollectionRef.document(questionId).get().await()
                val questionType = document.getString("type")?.toQuestionType()
                val response = when (questionType) {
                    is QuestionType.Choice -> document.toObject(ChoiceQuestionDTO::class.java)
                    is QuestionType.Blank -> document.toObject(BlankQuestionDTO::class.java)
                    else -> throw Exception("잘못된 문제 타입입니다.")
                }
                requireNotNull(response).toVO(questionId)
            }
        }

    override suspend fun getQuestion(questionId: String): Result<Question> = runCatching {
        val document = questionCollectionRef.document(questionId).get().await()
        val questionType = document.getString("type")?.toQuestionType()
        val response = when (questionType) {
            is QuestionType.Choice -> document.toObject(ChoiceQuestionDTO::class.java)
            is QuestionType.Blank -> document.toObject(BlankQuestionDTO::class.java)
            else -> throw Exception("잘못된 문제 타입입니다.")
        }
        requireNotNull(response).toVO(questionId)
    }

    override fun observeQuestion(questionId: String): Flow<Question> = callbackFlow {
        val listener = questionCollectionRef.document(questionId).addSnapshotListener { value, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            val questionType = value?.getString("type")?.toQuestionType()
            val response = when (questionType) {
                is QuestionType.Choice -> value?.toObject(ChoiceQuestionDTO::class.java)
                is QuestionType.Blank -> value?.toObject(BlankQuestionDTO::class.java)
                else -> throw Exception("잘못된 문제 타입입니다.")
            }?.toVO(questionId)

            if (response != null) {
                trySend(response)
            }
        }

        awaitClose { listener.remove() }
    }

    override suspend fun getRealTimeQuestions(questionIds: List<String>): Result<List<Flow<Question>>> =
        runCatching {
            questionIds.map { questionId ->
                observeQuestion(questionId)
            }
        }

    override suspend fun createQuestion(choiceQuestionCreationInfo: ChoiceQuestionCreationInfo): Result<String> =
        runCatching {
            val document = questionCollectionRef.add(choiceQuestionCreationInfo.toDTO()).await()
            document.id
        }

    override suspend fun deleteQuestions(questionIds: List<String>): Result<Unit> = runCatching {
        questionIds.forEach { questionId ->
            questionCollectionRef.document(questionId).delete().await()
        }
    }

    override suspend fun updateCurrentSubmit(userId: String?, questionId: String, userAnswer: Any?): Result<Unit> =
        runCatching {
            val document = questionCollectionRef.document(questionId)
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(document)

                if (snapshot.exists()) {
                    when (userAnswer) {
                        is Int -> {
                            val choices = snapshot.get("user_answers") as? MutableList<Int> ?: throw Exception("user_answers 배열이 존재하지 않거나 잘못되었습니다.")
                            if (userAnswer in 0..4) {
                                choices[userAnswer] += 1
                                transaction.update(document, "user_answers", choices)
                            } else {
                                // no - op
                            }
                        }

                        is Map<*, *> -> {
                            transaction.update(document, "user_answers", FieldValue.arrayUnion(userId))
                        }

                        else -> throw Exception("잘못된 유저 답변입니다.")
                    }
                } else {
                    throw Exception("문서가 존재하지 않습니다.")
                }
            }.await()
        }

    override suspend fun createBlankQuestion(blankQuestionCreationInfo: BlankQuestionCreationInfo): Result<String> =
        runCatching {
            val document = questionCollectionRef.add(blankQuestionCreationInfo.toDTO()).await()
            document.id
        }
}
