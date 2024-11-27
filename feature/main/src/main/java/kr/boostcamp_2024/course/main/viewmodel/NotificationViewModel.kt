package kr.boostcamp_2024.course.main.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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
    val snackBarMessage: String? = null,
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

    private fun loadNotifications() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            authRepository.getUserKey().onSuccess { userKey ->
                notificationRepository.getNotifications(userKey)
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
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                snackBarMessage = "알림을 불러오지 못하였습니다.",
                            )
                        }
                    }
            }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            snackBarMessage = "유저키를 불러오지 못하였습니다.",
                        )
                    }
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
                .onFailure { throwable ->
                    Log.e("NotificationViewModel", "실패: $throwable")
                    _uiState.update { it.copy(snackBarMessage = "알림 삭제를 실패하였습니다.") }
                }
        }
    }

    fun acceptInvitation(notification: Notification) {
        viewModelScope.launch {
            userRepository.addStudyGroupToUser(notification.userid, notification.groupId)
                .onSuccess {
                    deleteInvitation(notification.id)
                    addGroupMember(notification.userid, notification.groupId)
                }
                .onFailure { throwable ->
                    Log.e("NotificationViewModel", "실패: $throwable")
                    _uiState.update { it.copy(snackBarMessage = "알림 수락을 실패하였습니다.") }
                }
        }
    }

    private fun addGroupMember(userId: String, groupId: String) {
        viewModelScope.launch {
            studyGroupRepository.addUser(groupId, userId).onSuccess {
            }.onFailure { throwable ->
                Log.e("NotificationViewModel", "실패: $throwable")
                _uiState.update { it.copy(snackBarMessage = "그룹원을 그룹에 추가하는데 실패하였습니다.") }
            }
        }
    }

    fun onSnackBarShown() {
        _uiState.update { it.copy(snackBarMessage = null) }
    }

}
