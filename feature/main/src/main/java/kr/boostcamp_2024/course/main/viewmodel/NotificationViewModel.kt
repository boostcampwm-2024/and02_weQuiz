package kr.boostcamp_2024.course.main.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.model.Notification
import kr.boostcamp_2024.course.domain.model.NotificationWithGroupInfo
import kr.boostcamp_2024.course.domain.repository.NotificationRepository
import kr.boostcamp_2024.course.domain.repository.StudyGroupRepository
import kr.boostcamp_2024.course.domain.repository.UserRepository
import javax.inject.Inject

data class NotificationUiState(
    val isLoading: Boolean = false,
    val notificationWithGroupInfoList: List<NotificationWithGroupInfo> = emptyList(),
)



@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository,
    private val studyGroupRepository: StudyGroupRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<NotificationUiState> = MutableStateFlow(NotificationUiState())
    val uiState: MutableStateFlow<NotificationUiState> = _uiState

    init {
        loadNotifications()
    }

    private fun loadNotifications() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            notificationRepository.getNotifications("M2PzD8bxVaDAwNrLhr6E")
                .onSuccess { notifications ->

                    val notificationWithStudyGroupNameList = notifications.map {
                        val studyGroupNameResult = studyGroupRepository.getStudyGroupName(it.groupId)
                        val notificationWithGroupInfo =
                            NotificationWithGroupInfo(it, studyGroupNameResult.getOrNull() ?: "")
                        notificationWithGroupInfo
                    }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            notificationWithGroupInfoList = notificationWithStudyGroupNameList,
                        )
                    }

                }
                .onFailure {
                    _uiState.update { it.copy(isLoading = false) }
                }
        }
    }

    fun deleteInvitation(studyId: String) {
        viewModelScope.launch {
            notificationRepository.deleteNotification(studyId)
                .onSuccess {
                    _uiState.update { currentState ->
                        val updatedList = currentState.notificationWithGroupInfoList.filterNot { it.notification.id == studyId }
                        currentState.copy(notificationWithGroupInfoList = updatedList)
                    }
                }
                .onFailure { Log.d("NotificationViewModel", "실패: $it") }
        }
    }

    fun acceptInvitation(notification: Notification) {
        viewModelScope.launch {
            userRepository.addStudyGroupToUser(notification.userid, notification.groupId)
                .onSuccess {
                    deleteInvitation(notification.id)
                }
                .onFailure { Log.d("NotificationViewModel", "실패: $it") }
        }
    }
}

