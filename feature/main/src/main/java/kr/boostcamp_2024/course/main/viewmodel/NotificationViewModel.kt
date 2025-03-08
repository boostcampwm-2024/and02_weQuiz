package kr.boostcamp_2024.course.main.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.model.Notification
import kr.boostcamp_2024.course.domain.model.NotificationWithGroupInfo
import kr.boostcamp_2024.course.domain.repository.AuthRepository
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
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<NotificationUiState> = MutableStateFlow(NotificationUiState())
    val uiState: StateFlow<NotificationUiState> = _uiState
        .onStart {
            loadNotifications()
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            NotificationUiState(),
        )

    private val _errorFlow = MutableSharedFlow<Throwable>()
    val errorFlow = _errorFlow.asSharedFlow()

    private fun loadNotifications() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                val userKey = authRepository.getUserKey()
                val notifications = notificationRepository.getNotifications(userKey)
                val notificationWithStudyGroupNameList = notifications.map { notification ->
                    val studyGroup = studyGroupRepository.getStudyGroup(notification.groupId)
                    NotificationWithGroupInfo(
                        notification = notification,
                        studyGroupName = studyGroup.name,
                        studyGroupImgUrl = studyGroup.studyGroupImageUrl,
                    )
                }
                _uiState.update { it.copy(isLoading = false, notificationWithGroupInfoList = notificationWithStudyGroupNameList) }
            } catch (e: Exception) {
                _errorFlow.emit(e)
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun deleteInvitation(studyId: String) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                notificationRepository.deleteNotification(studyId)
                _uiState.update { currentState ->
                    val updatedList = currentState.notificationWithGroupInfoList.filterNot { it.notification.id == studyId }
                    currentState.copy(isLoading = false, notificationWithGroupInfoList = updatedList)
                }
            } catch (e: Exception) {
                _errorFlow.emit(e)
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    fun acceptInvitation(notification: Notification) {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                userRepository.addStudyGroupToUser(notification.userId, notification.groupId)
                deleteInvitation(notification.id)
                studyGroupRepository.addUser(notification.groupId, notification.userId)

                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _errorFlow.emit(e)
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}
