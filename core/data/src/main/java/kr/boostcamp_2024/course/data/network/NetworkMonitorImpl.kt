package kr.boostcamp_2024.course.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.NetworkMonitor
import kr.boostcamp_2024.course.domain.NetworkState
import javax.inject.Inject

class NetworkMonitorImpl @Inject constructor(
    appContext: Context,
) : NetworkMonitor {
    private val _networkState = MutableStateFlow<NetworkState>(NetworkState.None)
    override val networkState: StateFlow<NetworkState> = _networkState

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val validTransportTypes = listOf(
        NetworkCapabilities.TRANSPORT_WIFI,
        NetworkCapabilities.TRANSPORT_CELLULAR,
    )

    private val connectivityManager: ConnectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            _networkState.value = NetworkState.Connected
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            scope.launch {
                delay(NETWORK_CHECK_DELAY)
                if (isNetworkAvailable().not()) {
                    _networkState.value = NetworkState.NotConnected
                }
            }
        }
    }

    init {
        initiateNetworkState()
        registerNetworkCallback()
    }

    private fun initiateNetworkState() {
        _networkState.value = if (isNetworkAvailable()) {
            NetworkState.Connected
        } else {
            NetworkState.NotConnected
        }
    }

    private fun registerNetworkCallback() {
        NetworkRequest.Builder().apply {
            validTransportTypes.forEach { addTransportType(it) }
        }.let {
            connectivityManager.registerNetworkCallback(it.build(), networkCallback)
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        if (capabilities != null) {
            return validTransportTypes.any { capabilities.hasTransport(it) }
        }

        return false
    }

    companion object {
        private const val NETWORK_CHECK_DELAY = 1000L
    }
}
