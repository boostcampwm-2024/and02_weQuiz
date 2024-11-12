package kr.boostcamp_2024.course.main.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.repository.StudyGroupRepository
import kr.boostcamp_2024.course.domain.repository.UserRepository
import kr.boostcamp_2024.course.main.model.MainUiState
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val studyGroupRepository: StudyGroupRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<MainUiState> = MutableStateFlow(MainUiState.Loading)
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        loadStudyGroups()
    }

    private fun loadStudyGroups() {
        viewModelScope.launch {
            _uiState.value = MainUiState.Loading

            userRepository.getUser("M2PzD8bxVaDAwNrLhr6E")  // TODO: getCurrentUser
                .onSuccess { currentUser ->
                    val studyGroupIds = currentUser.studyGroups

                    studyGroupRepository.getStudyGroup(studyGroupIds)
                        .onSuccess { studyGroups ->
                            _uiState.value = MainUiState.Success(currentUser, studyGroups)
                        }
                        .onFailure {
                            Log.e("MainViewModel", "Failed to load study groups", it)
                            /* error */
                        }
                }
                .onFailure {
                    Log.e("MainViewModel", "Failed to load user", it)
                    /* error */
                }
        }
    }
}