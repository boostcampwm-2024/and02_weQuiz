package kr.boostcamp_2024.course.study.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.model.Category
import kr.boostcamp_2024.course.domain.model.StudyGroup
import kr.boostcamp_2024.course.domain.model.User
import kr.boostcamp_2024.course.domain.repository.CategoryRepository
import kr.boostcamp_2024.course.domain.repository.StudyGroupRepository
import kr.boostcamp_2024.course.domain.repository.UserRepository
import kr.boostcamp_2024.course.study.R
import javax.inject.Inject

data class DetailStudyUiState(
    val isLoading: Boolean = false,
    val currentGroup: StudyGroup? = null,
    val errorMessageId: Int? = null,
    val categories: List<Category> = emptyList(),
    val users: List<User> = emptyList(),
    val owner: User? = null,
)

@HiltViewModel
class DetailStudyViewModel @Inject constructor(
    private val studyGroupRepository: StudyGroupRepository,
    private val userRepository: UserRepository,
    private val categoryRepository: CategoryRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<DetailStudyUiState> = MutableStateFlow(DetailStudyUiState())
    val uiState: StateFlow<DetailStudyUiState> = _uiState.asStateFlow()

    init {
        loadStudyGroup()
    }

    private fun loadStudyGroup() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            // TODO: getCurrentGroup
            studyGroupRepository.getStudyGroup("Jn1m6Pr8B3a9gfZvNLKB")
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
                }
                .onFailure { it ->
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

    fun shownErrorMessage() {
        _uiState.update { it.copy(errorMessageId = null) }
    }
}
