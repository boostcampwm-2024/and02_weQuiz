package kr.boostcamp_2024.course.study.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.model.Category
import kr.boostcamp_2024.course.domain.model.StudyGroup
import kr.boostcamp_2024.course.domain.model.User
import kr.boostcamp_2024.course.domain.repository.AuthRepository
import kr.boostcamp_2024.course.domain.repository.CategoryRepository
import kr.boostcamp_2024.course.domain.repository.NotificationRepository
import kr.boostcamp_2024.course.domain.repository.QuestionRepository
import kr.boostcamp_2024.course.domain.repository.QuizRepository
import kr.boostcamp_2024.course.domain.repository.StudyGroupRepository
import kr.boostcamp_2024.course.domain.repository.UserOmrRepository
import kr.boostcamp_2024.course.domain.repository.UserRepository
import kr.boostcamp_2024.course.study.R
import kr.boostcamp_2024.course.study.navigation.StudyRoute
import javax.inject.Inject

data class DetailStudyUiState(
    val isLoading: Boolean = false,
    val currentGroup: StudyGroup? = null,
    val errorMessageId: Int? = null,
    val categories: List<Category> = emptyList(),
    val users: List<User> = emptyList(),
    val owner: User? = null,
    val userId: String? = null,
    val isDeleteStudyGroupSuccess: Boolean = false,
    val isLeaveStudyGroupSuccess: Boolean = false,
)

@HiltViewModel
class DetailStudyViewModel @Inject constructor(
    private val studyGroupRepository: StudyGroupRepository,
    private val userRepository: UserRepository,
    private val categoryRepository: CategoryRepository,
    private val authRepository: AuthRepository,
    private val notificationRepository: NotificationRepository,
    private val quizRepository: QuizRepository,
    private val questionRepository: QuestionRepository,
    private val userOmrRepository: UserOmrRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val studyGroupId: String = savedStateHandle.toRoute<StudyRoute>().studyGroupId

    private val _uiState: MutableStateFlow<DetailStudyUiState> = MutableStateFlow(DetailStudyUiState())
    val uiState: StateFlow<DetailStudyUiState> = _uiState.onStart {
        loadCurrentUser()
        loadStudyGroup()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), DetailStudyUiState())

    private fun loadCurrentUser() {
        viewModelScope.launch {
            authRepository.getUserKey()
                .onSuccess { userKey ->
                    _uiState.update { it.copy(userId = userKey) }
                }.onFailure {
                    Log.e("DetailStudyViewModel", "Failed to get user key", it)
                }
        }
    }

    private fun loadStudyGroup() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            studyGroupRepository.getStudyGroup(studyGroupId)
                .onSuccess { currentGroup ->
                    val ownerId = currentGroup.ownerId
                    val categoryIds = currentGroup.categories
                    val userIds = currentGroup.users
                    loadCategories(currentGroup, categoryIds)
                    loadUsers(currentGroup, userIds)
                    loadOwner(currentGroup, ownerId)
                }.onFailure {
                    Log.e("DetailStudyViewModel", "Failed to load study group", it)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessageId = R.string.error_message_load_study_group,
                        )
                    }
                }
            getStudyGroup(studyGroupId)
        }
    }

    private fun getStudyGroup(studyGroupId: String) {
        viewModelScope.launch {
            studyGroupRepository.getStudyGroup(studyGroupId)
                .onSuccess { currentGroup ->
                    val ownerId = currentGroup.ownerId
                    val categoryIds = currentGroup.categories
                    val userIds = currentGroup.users
                    loadCategories(currentGroup, categoryIds)
                    loadUsers(currentGroup, userIds)
                    loadOwner(currentGroup, ownerId)
                }.onFailure {
                    Log.e("DetailStudyViewModel", "Failed to load study group", it)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessageId = R.string.error_message_load_study_group,
                        )
                    }
                }
        }
    }

    private fun loadCategories(currentGroup: StudyGroup, categoryIds: List<String>) {
        viewModelScope.launch {
            categoryRepository.getCategories(categoryIds)
                .onSuccess { categories ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            currentGroup = currentGroup,
                            categories = categories,
                        )
                    }
                }.onFailure { it ->
                    Log.e("DetailStudyViewModel", "Failed to load categories", it)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessageId = R.string.error_message_load_categories,
                        )
                    }
                }
        }
    }

    private fun loadUsers(currentGroup: StudyGroup, userIds: List<String>) {
        viewModelScope.launch {
            userRepository.getUsers(userIds)
                .onSuccess { users ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            currentGroup = currentGroup,
                            users = users,
                        )
                    }
                }.onFailure {
                    Log.e("DetailStudyViewModel", "Failed to load users", it)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessageId = R.string.error_message_load_users,
                        )
                    }
                }
        }
    }

    private fun loadOwner(currentGroup: StudyGroup, ownerId: String) {
        viewModelScope.launch {
            userRepository.getUser(ownerId)
                .onSuccess { owner ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            currentGroup = currentGroup,
                            owner = owner,
                        )
                    }
                }.onFailure {
                    Log.e("DetailStudyViewModel", "Failed to load users", it)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessageId = R.string.error_message_load_owner,
                        )
                    }
                }
        }
    }

    fun addNotification(groupId: String, email: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            userRepository.findUserByEmail(email)
                .onSuccess {
                    notificationRepository.addNotification(groupId, it.id)
                        .onSuccess {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                )
                            }
                        }.onFailure {
                            Log.e("DetailStudyViewModel", "Failed to add notification", it)
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    errorMessageId = R.string.error_message_add_notification,
                                )
                            }
                        }
                }.onFailure {
                    Log.e("DetailStudyViewModel", "Failed to find user", it)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessageId = R.string.error_message_find_user,
                        )
                    }
                }
        }
    }

    fun deleteStudyGroupMember(userId: String, studyGroupId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            studyGroupRepository.deleteUser(studyGroupId, userId)
                .onSuccess {
                    deleteUserGroup(userId, studyGroupId)

                }.onFailure {
                    Log.e("DetailStudyViewModel", "Failed to delete study group member", it)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessageId = R.string.error_message_delete_study_group_member,
                        )
                    }
                }
        }
    }

    private fun deleteUserGroup(userId: String, groupId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            userRepository.deleteStudyGroupUser(userId, groupId)
                .onSuccess {
                    loadStudyGroup()
                }.onFailure {
                    Log.e("DetailStudyViewModel", "Failed to delete study group member", it)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessageId = R.string.error_message_delete_users_group,
                        )
                    }
                }
        }
    }

    fun deleteStudyGroup() {
        uiState.value.currentGroup?.let { studyGroup ->
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
                                                        categoryRepository.deleteCategories(categories.map { it.id })
                                                            .onSuccess {
                                                                notificationRepository.deleteNotification(notificationId = studyGroup.id)
                                                                    .onSuccess {
                                                                        userRepository.deleteStudyGroupUsers(studyGroup.users, studyGroup.id)
                                                                            .onSuccess {
                                                                                studyGroupRepository.deleteStudyGroup(studyGroup.id)
                                                                                    .onSuccess {
                                                                                        Log.d("DetailStudyViewModel", "스터디 그룹 삭제 완료")
                                                                                        _uiState.update { it.copy(isDeleteStudyGroupSuccess = true) }
                                                                                    }
                                                                                    .onFailure {
                                                                                        Log.e("DetailStudyViewModel", "Failed to remove study group", it)
                                                                                        _uiState.update { it.copy(errorMessageId = R.string.error_message_delete_study_group) }
                                                                                    }
                                                                            }
                                                                            .onFailure {
                                                                                Log.e("DetailStudyViewModel", "Failed to remove users from study group", it)
                                                                                _uiState.update { it.copy(errorMessageId = R.string.error_message_delete_study_group) }
                                                                            }
                                                                    }
                                                                    .onFailure {
                                                                        Log.e("DetailStudyViewModel", "Failed to remove notification", it)
                                                                        _uiState.update { it.copy(errorMessageId = R.string.error_message_delete_study_group) }
                                                                    }
                                                            }
                                                            .onFailure {
                                                                Log.e("DetailStudyViewModel", "Failed to remove categories from study group", it)
                                                                _uiState.update { it.copy(errorMessageId = R.string.error_message_delete_study_group) }
                                                            }
                                                    }
                                                    .onFailure {
                                                        Log.e("DetailStudyViewModel", "Failed to remove quizzes from study group", it)
                                                        _uiState.update { it.copy(errorMessageId = R.string.error_message_delete_study_group) }
                                                    }
                                            }
                                            .onFailure {
                                                Log.e("DetailStudyViewModel", "Failed to remove userOmr from study group", it)
                                                _uiState.update { it.copy(errorMessageId = R.string.error_message_delete_study_group) }
                                            }
                                    }
                                    .onFailure {
                                        Log.e("DetailStudyViewModel", "Failed to remove questions from study group", it)
                                        _uiState.update { it.copy(errorMessageId = R.string.error_message_delete_study_group) }
                                    }
                            }
                            .onFailure {
                                Log.e("DetailStudyViewModel", "Failed to remove quizzes from study group", it)
                                _uiState.update { it.copy(errorMessageId = R.string.error_message_delete_study_group) }
                            }
                    }
                    .onFailure {
                        Log.e("DetailStudyViewModel", "Failed to remove categories from study group", it)
                        _uiState.update { it.copy(errorMessageId = R.string.error_message_delete_study_group) }
                    }
            }
        }
    }

    fun deleteUserFromStudyGroup() {
        val userId = uiState.value.userId ?: return
        val studyGroupId = uiState.value.currentGroup?.id ?: return

        viewModelScope.launch {
            userRepository.deleteStudyGroupUser(userId, studyGroupId)
                .onSuccess {
                    studyGroupRepository.deleteUser(studyGroupId, userId)
                        .onSuccess {
                            _uiState.update { it.copy(isLeaveStudyGroupSuccess = true) }
                        }
                        .onFailure {
                            Log.e("MainViewModel", "Failed to remove user from study group", it)
                            _uiState.update { it.copy(errorMessageId = R.string.error_message_delete_study_group) }
                        }
                }
                .onFailure {
                    Log.e("MainViewModel", "Failed to remove user from study group", it)
                    _uiState.update { it.copy(errorMessageId = R.string.error_message_delete_study_group) }
                }
        }
    }

    fun shownErrorMessage() {
        _uiState.update { it.copy(errorMessageId = null) }
    }
}
