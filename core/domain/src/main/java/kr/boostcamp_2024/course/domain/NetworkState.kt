package kr.boostcamp_2024.course.domain

sealed class NetworkState {
    data object None : NetworkState()

    data object Connected : NetworkState()

    data object NotConnected : NetworkState()
}
