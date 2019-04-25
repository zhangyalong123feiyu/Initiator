package com.lenovo.Initiator.utils

import android.app.ActivityManager
import android.content.Context
import android.text.TextUtils

//
// for judge the service is live
//

object ServiceIsRunUtil {

    fun isServiceRun(context: Context, packName: String): Boolean {
        if (TextUtils.isEmpty(packName)) {
            return false
        }

        var activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        var runningServiceInfo = activityManager.getRunningServices(30)
        for (element in runningServiceInfo) {
            if (element.service.className.toString().equals(packName)) {
                return true
            }
        }
        return false
    }
}