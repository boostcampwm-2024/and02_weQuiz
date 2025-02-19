package kr.boostcamp_2024.course.domain

import kotlinx.coroutines.flow.StateFlow

interface NetworkMonitor {
    val networkState: StateFlow<NetworkState>

    fun checkCurrentNetworkState()
}
