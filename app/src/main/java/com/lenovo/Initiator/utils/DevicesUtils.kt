package com.lenovo.Initiator.utils

import android.app.ActivityManager
import android.content.Context
import android.text.TextUtils
import android.util.Log
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.OutputStream

class DevicesUtils {
    companion object {
        fun installApp(context: Context, filePath: String): Boolean {
            if (TextUtils.isEmpty(filePath))
                throw IllegalArgumentException("Please check apk file path!")
            var result = false
            var process: Process? = null
            var outputStream: OutputStream? = null
            var errorStream: BufferedReader? = null
            try {
                process = Runtime.getRuntime().exec("su")
                outputStream = process!!.outputStream

                val command = "pm install -r $filePath\n"
                outputStream!!.write(command.toByteArray())
                outputStream.flush()
                outputStream.write("exit\n".toByteArray())
                outputStream.flush()
                process.waitFor()
                errorStream = BufferedReader(InputStreamReader(process.errorStream))
                val msg = StringBuilder()
                var line: String = errorStream.readLine()
                while (line != null) {
                    msg.append(line)
                }
                Log.d("TAG", "install msg is $msg")
                if (!msg.toString().contains("Failure")) {
                    result = true
                }
            } catch (e: Exception) {
                Log.i("TAG", e.message)
            } finally {
                try {
                    outputStream?.close()
                    errorStream?.close()
                } catch (e: IOException) {
                    outputStream = null
                    errorStream = null
                    process!!.destroy()
                }

            }
            val LaunchIntent = context.getPackageManager().getLaunchIntentForPackage("com.github.lzyzsd.jsbridge.example")
            context.startActivity(LaunchIntent)
            return result
        }

        fun reStart() {
            Log.i("TAG", "do restart command")
            val arrayRestart = arrayOf("su", "-c", "reboot")
            try {
                val process = Runtime.getRuntime().exec(arrayRestart)
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }

        }

        fun killApp(context: Context, packgeName: String) {

            var am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            var infos = am.getRunningAppProcesses()
            for (info: ActivityManager.RunningAppProcessInfo in infos) {
                Log.i("TAG", " android pid has==" + info.pid)
                if (info.processName.equals(packgeName)) {
                    android.os.Process.killProcess(info.pid)
                    RestartUtils.killApps(info.pid.toString())
                    System.exit(0)
                }
            }
        }
    }
}

