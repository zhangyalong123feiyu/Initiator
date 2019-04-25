package com.lenovo.Initiator.model

import android.util.Log
import com.lenovo.Initiator.protocol.Config
import com.lenovo.Initiator.utils.AdbConnectUtils

class DeviceInitiator() {

    //
    // Read config then start apps.
    //

    fun startApps() {
        val configSize = Config.appList.size
        for (i in 0 until configSize) {
            if (Config.appList.get(i).isAutoStart) {
                AdbConnectUtils.execRootCmd("am start -n " + Config.appList.get(i).startAppParam)
                Log.i("TAG", "do start command====" + "am start -n " + Config.appList.get(i).getStartAppParam())
            }
        }
    }
}