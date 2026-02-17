package ru.pomidorka.weatherapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.concurrent.ConcurrentHashMap

sealed interface NetworkState {
    data object Connected : NetworkState
    data object Disconnected : NetworkState
}

@Stable
class NetworkChecker(context: Context) {
    private val _networkState = MutableStateFlow<NetworkState>(NetworkState.Disconnected)
    val networkState = _networkState.asStateFlow()

    val isConnected
        get() = networkState.value == NetworkState.Connected

    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val validNetworks = ConcurrentHashMap.newKeySet<Network>()

    private val callback = object : ConnectivityManager.NetworkCallback() {
        override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
            val isValid = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

            if (isValid) {
                validNetworks.add(network)
            } else {
                validNetworks.remove(network)
            }
            updateState()
        }

        override fun onLost(network: Network) {
            validNetworks.remove(network)
            updateState()
        }
    }

    init {
        _networkState.update {
            if (hasValidNetwork()) NetworkState.Connected else NetworkState.Disconnected
        }
    }

    private fun hasValidNetwork(): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
    }

    private fun updateState() {
        val isConnected = validNetworks.isNotEmpty()
        _networkState.value = if (isConnected) NetworkState.Connected else NetworkState.Disconnected
    }

    fun register() {
        connectivityManager.registerNetworkCallback(
            NetworkRequest.Builder().build(),
            callback
        )
    }

    fun unregister() {
        connectivityManager.unregisterNetworkCallback(
            callback
        )
        validNetworks.clear()
    }
}