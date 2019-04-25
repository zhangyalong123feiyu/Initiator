package com.lenovo.Initiator.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.util.Log
import com.lenovo.Initiator.protocol.Config
import com.lenovo.Initiator.utils.NetworkUtils
import java.util.*

//
// BroadcastReceiver receives broadcast. DeviceReceiver starts app and restarts pad.
//

class DeviceReceiver : BroadcastReceiver() {

    private lateinit var timer: Timer // timer for restarting pad.

    override fun onReceive(context: Context, intent: Intent) {

        //
        // start app when pad is started.
        //

        if (intent.action.equals(Intent.ACTION_BOOT_COMPLETED)) {
            var startIntent = Intent(context, MainActivity::class.java)
            startIntent.action = "android.intent.action.MAIN"
            startIntent.addCategory("android.intent.category.LAUNCHER")
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(startIntent)

            //
            // restart pad when wifi is not available
            //

        } else if (WifiManager.NETWORK_STATE_CHANGED_ACTION == intent.action) {
            Log.i("TAG", "network value is==" + NetworkUtils.isAvaliable(context))
            if (Config.restartPadIfBadWifi) {
                if (!NetworkUtils.isAvaliable(context)) {
                    Log.i("TAG", "network change is not avaliable")
                    timer = Timer()
                    timer.schedule(object : TimerTask() {
                        override fun run() {
                            if (!NetworkUtils.isAvaliable(context)) {
                                // DevicesUtils.reStart()
                                Log.i("TAG", "network is not avaliable for 3 minutes later")
                            }
                        }
                    }, 3 * 60 * 1000)
                }

            }
        }

    }
}