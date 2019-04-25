package com.lenovo.Initiator.view

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Binder
import android.os.IBinder
import android.util.Log

//
// protect working process
//

class ServiceProtector : Service() {

    private lateinit var intent: Intent

    private var TAG = ServiceProtector::class.java.simpleName

    private var connection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            intent = Intent(this@ServiceProtector, DeviceService::class.java)
            bindService(intent, this, Context.BIND_AUTO_CREATE)
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i(TAG, "protectserver is connected")

        }
    }

    //
    //  Create a binder to bind the service
    //

    inner class DevicePBinder: Binder(){
        fun getService() :ServiceProtector{
            return this@ServiceProtector
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return DevicePBinder()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var flag = Service.START_FLAG_RETRY
        return super.onStartCommand(intent, flag, startId)
    }

}