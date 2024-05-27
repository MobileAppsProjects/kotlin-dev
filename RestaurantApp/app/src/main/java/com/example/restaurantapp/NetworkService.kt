package com.example.restaurantapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Handler
import android.os.Looper

class NetworkService(private val context: Context) {
    private var networkReceiver: NetworkReceiver? = null

    fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    fun registerNetworkReceiver(onNetworkChange: (Boolean) -> Unit) {
        networkReceiver = NetworkReceiver(onNetworkChange)
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        context.registerReceiver(networkReceiver, intentFilter)
    }

    fun unregisterNetworkReceiver() {
        networkReceiver?.let {
            context.unregisterReceiver(it)
        }
    }

    fun checkNetworkStatusAsync(onResult: (Boolean) -> Unit) {
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            onResult(isConnected())
        }
    }

    private inner class NetworkReceiver(private val onNetworkChange: (Boolean) -> Unit) : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = connectivityManager.activeNetwork
            val capabilities = connectivityManager.getNetworkCapabilities(network)
            val isConnected = capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false

            // Llamar a la función de retorno de llamada con el estado de la conexión
            onNetworkChange(isConnected)
        }
    }
}
