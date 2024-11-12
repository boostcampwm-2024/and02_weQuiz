package kr.boostcamp_2024.course.main.model

import kr.boostcamp_2024.course.domain.model.StudyGroup
import kr.boostcamp_2024.course.domain.model.User

sealed interface MainUiState {

    data object Loading : MainUiState

    data class Success(
        val currentUser: User,
        val studyGroups: List<StudyGroup>
    ) : MainUiState
}