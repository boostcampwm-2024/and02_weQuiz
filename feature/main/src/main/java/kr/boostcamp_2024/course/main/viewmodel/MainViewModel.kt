package kr.boostcamp_2024.course.main.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.model.StudyGroup
import kr.boostcamp_2024.course.domain.model.User
import kr.boostcamp_2024.course.domain.repository.AuthRepository
import kr.boostcamp_2024.course.domain.repository.CategoryRepository
import kr.boostcamp_2024.course.domain.repository.NotificationRepository
import kr.boostcamp_2024.course.domain.repository.QuestionRepository
import kr.boostcamp_2024.course.domain.repository.QuizRepository
import kr.boostcamp_2024.course.domain.repository.StorageRepository
import kr.boostcamp_2024.course.domain.repository.StudyGroupRepository
import kr.boostcamp_2024.course.domain.repository.UserOmrRepository
import kr.boostcamp_2024.course.domain.repository.UserRepository
import javax.inject.Inject

data class MainUiState(
    val isLoading: Boolean = false,
    val currentUser: User? = null,
    val studyGroups: List<StudyGroup> = emptyList(),
    val errorMessage: String? = null,
    val isLogout: Boolean = false,
    val notificationNumber: Int = 0,
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    private val studyGroupRepository: StudyGroupRepository,
    private val categoryRepository: CategoryRepository,
    private val quizRepository: QuizRepository,
    private val questionRepository: QuestionRepository,
    private val userOmrRepository: UserOmrRepository,
    private val notificationRepository: NotificationRepository,
    private val storageRepository: StorageRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    fun loadCurrentUser() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            authRepository.getUserKey()
                .onSuccess { currentUserId ->
                    userRepository.getUser(currentUserId)
                        .onSuccess { currentUser ->
                            loadStudyGroups(currentUser)
                        }
                        .onFailure {
                            Log.e("MainViewModel", "Failed to load user", it)
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessage = "유저 로드에 실패했습니다.",
                                )
                            }
                        }
                    loadNotifications(currentUserId)
                }
                .onFailure {
                    Log.e("MainViewModel", "Failed to load current user", it)
                    _uiState.update { it.copy(isLoading = false, errorMessage = "로그인한 유저가 없습니다.") }
                }
        }
    }

    private fun loadNotifications(userId: String) {
        viewModelScope.launch {
            notificationRepository.getNotifications(userId).onSuccess { notificationList ->
                _uiState.update { it.copy(notificationNumber = notificationList.size, isLoading = false) }
            }.onFailure { it ->
                Log.e("MainViewModel", "Failed to load notification numbers", it)
                _uiState.update { it.copy(notificationNumber = 0, isLoading = false, errorMessage = "알림 수 로드에 실패했습니다.") }
            }
        }
    }

    private fun loadStudyGroups(currentUser: User) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val studyGroupIds = currentUser.studyGroups

            studyGroupRepository.getStudyGroups(studyGroupIds)
                .onSuccess { studyGroups ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            currentUser = currentUser,
                            studyGroups = studyGroups,
                        )
                    }
                }
                .onFailure {
                    Log.e("MainViewModel", "Failed to load study groups", it)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "스터디 그룹 로드에 실패했습니다.",
                        )
                    }
                }
        }
    }

    fun deleteStudyGroup(studyGroup: StudyGroup) {
        viewModelScope.launch {
            categoryRepository.getCategories(studyGroup.categories)
                .onSuccess { categories ->
                    quizRepository.getQuizList(categories.flatMap { it.quizzes })
                        .onSuccess { quizzes ->
                            questionRepository.deleteQuestions(quizzes.flatMap { it.questions })
                                .onSuccess {
                                    userOmrRepository.deleteUserOmrs(quizzes.flatMap { it.userOmrs })
                                        .onSuccess {
                                            quizRepository.deleteQuizzes(quizzes.map { it.id })
                                                .onSuccess {
                                                    storageRepository.deleteImages(
                                                        categories.map { it.categoryImageUrl }
                                                            .filterNotNull(),
                                                    )
                                                        .onSuccess {
                                                            categoryRepository.deleteCategories(
                                                                categories.map { it.id },
                                                            )
                                                                .onSuccess {
                                                                    notificationRepository.deleteNotification(
                                                                        notificationId = studyGroup.id,
                                                                    )
                                                                        .onSuccess {
                                                                            storageRepository.deleteImages(
                                                                                studyGroup.studyGroupImageUrl?.let {
                                                                                    listOf(it)
                                                                                } ?: emptyList(),
                                                                            )
                                                                                .onSuccess {
                                                                                    userRepository.deleteStudyGroupUsers(
                                                                                        studyGroup.users,
                                                                                        studyGroup.id,
                                                                                    )
                                                                                        .onSuccess {
                                                                                            studyGroupRepository.deleteStudyGroup(
                                                                                                studyGroup.id,
                                                                                            )
                                                                                                .onSuccess {
                                                                                                    loadCurrentUser() // 스터디 그룹 최신화
                                                                                                }
                                                                                                .onFailure {
                                                                                                    Log.e(
                                                                                                        "MainViewModel",
                                                                                                        "Failed to remove study group",
                                                                                                        it,
                                                                                                    )
                                                                                                    _uiState.update {
                                                                                                        it.copy(
                                                                                                            errorMessage = "스터디 그룹에서 나가지 못했습니다.",
                                                                                                        )
                                                                                                    }
                                                                                                }
                                                                                        }
                                                                                        .onFailure {
                                                                                            Log.e(
                                                                                                "MainViewModel",
                                                                                                "Failed to remove users from study group",
                                                                                                it,
                                                                                            )
                                                                                            _uiState.update {
                                                                                                it.copy(
                                                                                                    errorMessage = "스터디 그룹에서 나가지 못했습니다.",
                                                                                                )
                                                                                            }
                                                                                        }
                                                                                }
                                                                                .onFailure {
                                                                                    Log.e(
                                                                                        "MainViewModel",
                                                                                        "Failed to remove study group image",
                                                                                        it,
                                                                                    )
                                                                                    _uiState.update {
                                                                                        it.copy(
                                                                                            errorMessage = "스터디 그룹에서 나가지 못했습니다.",
                                                                                        )
                                                                                    }
                                                                                }
                                                                        }
                                                                        .onFailure {
                                                                            Log.e(
                                                                                "MainViewModel",
                                                                                "Failed to remove notification",
                                                                                it,
                                                                            )
                                                                            _uiState.update {
                                                                                it.copy(
                                                                                    errorMessage = "스터디 그룹에서 나가지 못했습니다.",
                                                                                )
                                                                            }
                                                                        }
                                                                }
                                                                .onFailure {
                                                                    Log.e(
                                                                        "MainViewModel",
                                                                        "Failed to remove categories from study group",
                                                                        it,
                                                                    )
                                                                    _uiState.update {
                                                                        it.copy(
                                                                            errorMessage = "스터디 그룹에서 나가지 못했습니다.",
                                                                        )
                                                                    }
                                                                }
                                                        }
                                                        .onFailure {
                                                            Log.e(
                                                                "MainViewModel",
                                                                "Failed to remove categories images from study group",
                                                                it,
                                                            )
                                                            _uiState.update { it.copy(errorMessage = "스터디 그룹에서 나가지 못했습니다.") }
                                                        }
                                                }.onFailure {
                                                    Log.e(
                                                        "MainViewModel",
                                                        "Failed to remove quizzes from study group",
                                                        it,
                                                    )
                                                    _uiState.update { it.copy(errorMessage = "스터디 그룹에서 나가지 못했습니다.") }
                                                }
                                        }
                                        .onFailure {
                                            Log.e(
                                                "MainViewModel",
                                                "Failed to remove userOmr from study group",
                                                it,
                                            )
                                            _uiState.update { it.copy(errorMessage = "스터디 그룹에서 나가지 못했습니다.") }
                                        }
                                }
                                .onFailure {
                                    Log.e(
                                        "MainViewModel",
                                        "Failed to remove questions from study group",
                                        it,
                                    )
                                    _uiState.update { it.copy(errorMessage = "스터디 그룹에서 나가지 못했습니다.") }
                                }
                        }
                        .onFailure {
                            Log.e("MainViewModel", "Failed to remove quizzes from study group", it)
                            _uiState.update { it.copy(errorMessage = "스터디 그룹에서 나가지 못했습니다.") }
                        }
                }
                .onFailure {
                    Log.e("MainViewModel", "Failed to remove categories from study group", it)
                    _uiState.update { it.copy(errorMessage = "스터디 그룹에서 나가지 못했습니다.") }
                }
        }
    }

    fun deleteUserFromStudyGroup(studyGroupId: String) {
        uiState.value.currentUser?.let { user ->
            viewModelScope.launch {
                userRepository.deleteStudyGroupUser(user.id, studyGroupId)
                    .onSuccess {
                        studyGroupRepository.deleteUser(studyGroupId, user.id)
                            .onSuccess {
                                loadCurrentUser()
                            }
                            .onFailure {
                                Log.e("MainViewModel", "Failed to remove user from study group", it)
                                _uiState.update { it.copy(errorMessage = "스터디 그룹에서 나가지 못했습니다.") }
                            }
                    }
                    .onFailure {
                        Log.e("MainViewModel", "Failed to remove user from study group", it)
                        _uiState.update { it.copy(errorMessage = "스터디 그룹에서 나가지 못했습니다.") }
                    }
            }
        }
    }

    fun logout() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            authRepository.removeUserKey()
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isLogout = true,
                        )
                    }
                }.onFailure {
                    Log.e("MainViewModel", "Failed to logout", it)
                    _uiState.update { it.copy(errorMessage = "로그아웃에 실패했습니다.") }
                }
        }
    }

    fun shownErrorMessage() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
