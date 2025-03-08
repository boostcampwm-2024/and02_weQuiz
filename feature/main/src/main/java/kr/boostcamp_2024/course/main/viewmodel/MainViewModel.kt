package kr.boostcamp_2024.course.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.model.StudyGroup
import kr.boostcamp_2024.course.domain.model.User
import kr.boostcamp_2024.course.domain.repository.AuthRepository
import kr.boostcamp_2024.course.domain.repository.CategoryRepository
import kr.boostcamp_2024.course.domain.repository.GuideRepository
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
    val isLogout: Boolean = false,
    val notificationNumber: Int = 0,
    val isGuideShown: Boolean = true,
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
    private val guideRepository: GuideRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val _errorFlow = MutableSharedFlow<Throwable>()
    val errorFlow = _errorFlow.asSharedFlow()

    init {
        checkGuideStatus()
    }

    fun loadCurrentUser() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                val currentUserId = authRepository.getUserKey()
                val currentUser = userRepository.getUser(currentUserId)

                val studyGroups = studyGroupRepository.getStudyGroups(currentUser.studyGroups)
                val notificationList = notificationRepository.getNotifications(currentUserId)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        notificationNumber = notificationList.size,
                        currentUser = currentUser,
                        studyGroups = studyGroups,
                    )
                }
            } catch (e: Exception) {
                _errorFlow.emit(e)
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun checkGuideStatus() {
        viewModelScope.launch {
            try {
                val isGuideShown = guideRepository.getGuideStatus()
                _uiState.update { it.copy(isGuideShown = isGuideShown) }
            } catch (e: Exception) {
                _errorFlow.emit(e)
            }
        }
    }

    fun onGuideShown() {
        viewModelScope.launch {
            guideRepository.setGuideStatus(true)
            _uiState.update { it.copy(isGuideShown = true) }
        }
    }

    fun deleteStudyGroup(studyGroup: StudyGroup) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                val categories = categoryRepository.getCategories(studyGroup.categories)
                val quizzes = quizRepository.getQuizList(categories.flatMap { it.quizzes })

                questionRepository.deleteQuestions(quizzes.flatMap { it.questions })
                userOmrRepository.deleteUserOmrs(quizzes.flatMap { it.userOmrs })
                quizRepository.deleteQuizzes(quizzes.map { it.id })
                storageRepository.deleteImages(categories.mapNotNull { it.categoryImageUrl })
                categoryRepository.deleteCategories(categories.map { it.id })
                notificationRepository.deleteNotification(notificationId = studyGroup.id)
                studyGroup.studyGroupImageUrl?.let { storageRepository.deleteImage(it) }
                userRepository.deleteStudyGroupUsers(studyGroup.users, studyGroup.id)
                studyGroupRepository.deleteStudyGroup(studyGroup.id)

                loadCurrentUser()
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _errorFlow.emit(e)
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun deleteUserFromStudyGroup(studyGroupId: String) {
        uiState.value.currentUser?.let { user ->
            viewModelScope.launch {
                try {
                    _uiState.update { it.copy(isLoading = true) }

                    userRepository.deleteStudyGroupUser(user.id, studyGroupId)
                    studyGroupRepository.deleteUser(studyGroupId, user.id)

                    loadCurrentUser()
                    _uiState.update { it.copy(isLoading = false) }
                } catch (e: Exception) {
                    _errorFlow.emit(e)
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                authRepository.logout()
                _uiState.update { it.copy(isLoading = false, isLogout = true) }
            } catch (e: Exception) {
                _errorFlow.emit(e)
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
