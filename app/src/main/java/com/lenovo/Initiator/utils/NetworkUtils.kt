package com.lenovo.Initiator.utils

import android.content.Context
import android.net.ConnectivityManager

object NetworkUtils {

    /**
     * network is avaliable
     * @param
     * @return
     */

    fun isAvaliable(act: Context): Boolean {

        val manager = act
                .applicationContext.getSystemService(
                Context.CONNECTIVITY_SERVICE) as ConnectivityManager ?: return false

        val networkinfo = manager.activeNetworkInfo

        return if (networkinfo == null || !networkinfo.isAvailable) {
            false
        } else true

    }


}