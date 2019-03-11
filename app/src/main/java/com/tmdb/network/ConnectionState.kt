package com.tmdb.network

import android.content.Context
import android.net.ConnectivityManager

class ConnectionState(val context: Context) {
    fun hasNetwork(): Boolean {
        var isConnected = false
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnected)
            isConnected = true
        return isConnected
    }
}