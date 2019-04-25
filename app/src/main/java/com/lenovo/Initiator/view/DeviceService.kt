package com.lenovo.Initiator.view

import android.app.Service
import android.content.Intent
import android.os.Environment
import android.os.IBinder
import android.util.Log
import java.net.*
import java.nio.charset.Charset
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.ServiceConnection
import android.os.Binder
import com.lenovo.Initiator.protocol.CommandEvent
import com.lenovo.Initiator.protocol.Config
import com.lenovo.Initiator.utils.AdbConnectUtils
import com.lenovo.Initiator.utils.DevicesUtils
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat
import java.util.*

//
// DeviceService supports commands (install, reboot, download, restartApp, killApp, readConfig).
//

class DeviceService : Service() {

    private lateinit var socket: DatagramSocket
    private var filePath = Environment.getExternalStorageDirectory().toString() + "/test.apk"
    private lateinit var intent: Intent
    private var TAG = DeviceService::class.java.simpleName
    private var serviceConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            bindService(intent, this, Context.BIND_AUTO_CREATE)
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i(TAG, "server is connected")
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "start server")

        val intent = Intent(this, ServiceProtector::class.java)
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE)

        // At the beginning to create the log file
        if(Config.log){
            val fileName=getFileName()
            AdbConnectUtils.execRootCmd("logcat -v time -f /sdcard/"+fileName+" &")
        }
    }

    //
    // udp connect for python script
    //

    private fun doConnectClient() {

        var thread = Thread(object : Runnable {
            override fun run() {
                Log.i(TAG, "is connected")
                try {
                    socket = DatagramSocket(9898);
                } catch (e: SocketException) {
                    e.printStackTrace();
                }
                var receBuf = ByteArray(1024)
                var packet = DatagramPacket(receBuf, receBuf.size)
                while (true) {
                    Thread.sleep(500)
                    try {
                        socket.receive(packet);
                        var receive = String(packet.getData(), 0, packet.getLength(), Charset.forName("utf-8"));
                        Log.i("TAG", "recive content：" + receive);
                        doNext(receive)
                    } catch (e: SocketException) {
                        e.printStackTrace();
                    }
                }

            }
        })
        thread.start()
    }

    private fun doNext(readLine: String?) {
        Log.i(TAG, "recive content=" + readLine)
        val commandEvent = CommandEvent(readLine)
        EventBus.getDefault().post(commandEvent)

        if (readLine.equals("install")) {
            DevicesUtils.installApp(this, filePath)

        } else if (readLine.equals("reboot")) {
            DevicesUtils.reStart()

        } else if (readLine.equals("download")) {
            val commandEvent = CommandEvent("download")
            EventBus.getDefault().post(commandEvent)

        } else if (readLine.equals("restartApp")) {

            val mgr = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            val restartIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
            assert(mgr != null)
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1000, restartIntent) // restart after one second
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0)
            System.gc()

        } else if (readLine.equals("killApp")) {          // Forced to stop billing
            AdbConnectUtils.execRootCmd("am force-stop com.lenovo.billing")
        } else if (readLine.equals("openAccess")) {       // init Adb
            AdbConnectUtils.execRootCmd("setprop service.adb.tcp.port 5555")
            AdbConnectUtils.execRootCmd("stop adbd")
            AdbConnectUtils.execRootCmd("start adbd")
        } else if (readLine.equals("createLog")){         // To create the log file
            val processName=AdbConnectUtils.execRootCmd("pidof name")
            Log.i("TAG","logcat name=="+processName)
            val fileName=getFileName()
            AdbConnectUtils.execRootCmd("logcat -v time -f /sdcard/"+fileName+" &")
        }
    }

    //
    // The log file name
    //

    private fun getFileName() :String {
        val date=Date()
        val dateFormate=SimpleDateFormat("yyyyMMdd-HHmm-")
        val fileName=dateFormate.format(date)+Config.logPostfix+".log"
        return fileName
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        var flag = Service.START_FLAG_RETRY
        doConnectClient()

        // doConnectServer()
        return super.onStartCommand(intent, flag, startId)
    }

    //
    // Function has no use for sit extension in the future
    //

    private fun doConnectServer() {
        var thread = Thread(object : Runnable {
            override fun run() {

                val date = "你好UDP".toByteArray()
                val inet = InetAddress.getByName("127.0.0.1")
                val dp = DatagramPacket(date, date.size, inet, 6000)
                // make DatagramSocket object，datapackage send and recvie object
                val ds = DatagramSocket()
                // send datapackage
                ds.send(dp)

                var receBuf = ByteArray(1024)
                var packet = DatagramPacket(receBuf, receBuf.size)
                while (true) {

                    Thread.sleep(500)
                    try {
                        ds.receive(packet);
                        var receive = String(packet.getData(), 0, packet.getLength(), Charset.forName("utf-8"));
                        Log.i("TAG", "recive content：" + receive);
                        doNext(receive)
                    } catch (e: SocketException) {
                        e.printStackTrace();
                    }
                }

                ds.close()
            }
        })
        thread.start()
    }

    //
    //  Create a binder to bind the service
    //

    inner class DeviceBinder: Binder(){
        fun getService() :DeviceService{
            return this@DeviceService
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return DeviceBinder()
    }


}