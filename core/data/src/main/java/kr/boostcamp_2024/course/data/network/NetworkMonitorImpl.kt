package kr.boostcamp_2024.course.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kr.boostcamp_2024.course.domain.NetworkMonitor
import javax.inject.Inject

class NetworkMonitorImpl @Inject constructor(
    @ApplicationContext appContext: Context,
) : NetworkMonitor {
    private val connectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val validTransportTypes = listOf(
        NetworkCapabilities.TRANSPORT_WIFI,
        NetworkCapabilities.TRANSPORT_CELLULAR,
    )

    override val networkState: Flow<Boolean> = callbackFlow {
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(true)
            }

            override fun onLost(network: Network) {
                launch {
                    delay(NETWORK_CHECK_DELAY)
                    if (isNetworkAvailable().not()) {
                        trySend(false)
                    }
                }
            }
        }

        val networkRequest = NetworkRequest.Builder()
            .apply {
                validTransportTypes.forEach { addTransportType(it) }
                addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            }
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(networkCallback) // 콜백 해제
        }
    }.flowOn(Dispatchers.IO)

    private fun isNetworkAvailable(): Boolean {
        val activeNetwork = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) && validTransportTypes.any(capabilities::hasTransport)
    }

    companion object {
        private const val NETWORK_CHECK_DELAY = 1000L
    }
}
