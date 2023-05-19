package ru.internetcloud.strava.data.internet.repository

import android.app.Application
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.core.content.ContextCompat
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import ru.internetcloud.strava.domain.internet.InternetStatusRepository
import timber.log.Timber

class InternetStatusRepositoryImpl : InternetStatusRepository {

    override fun observeInternetChange(application: Application): Flow<Boolean> {
        val connectivityManager = ContextCompat.getSystemService(application, ConnectivityManager::class.java)!!

        return callbackFlow {
            send(!isNoNetwork(application))

            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()

            val networkCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    availableNetworks.add(network.toString())
                    Timber.tag("rustam").d("onAvailable, trySendBlocking = true, network = $network")
                    Timber.tag("rustam").d("onAvailable, trySendBlocking = true, availableNetworks = ${availableNetworks.joinToString(",")}")
                    trySendBlocking(true)
                }

                override fun onLost(network: Network) {
                    availableNetworks.remove(network.toString())
                    Timber.tag("rustam").d("onLost, trySendBlocking = false, network = $network")
                    Timber.tag("rustam").d("onLost, trySendBlocking = false, availableNetworks = ${availableNetworks.joinToString(",")}")
                    if (availableNetworks.isEmpty()) {
                        trySendBlocking(false)
                    }
                }
            }

            connectivityManager.registerNetworkCallback(request, networkCallback)

            awaitClose {
                connectivityManager.unregisterNetworkCallback(networkCallback)
            }
        }
    }

    private fun isNoNetwork(application: Application): Boolean {
        val cm = ContextCompat.getSystemService(application, ConnectivityManager::class.java)
        return cm?.isActiveNetworkMetered ?: false
    }

    companion object {
        private val availableNetworks = mutableSetOf<String>()
    }
}
