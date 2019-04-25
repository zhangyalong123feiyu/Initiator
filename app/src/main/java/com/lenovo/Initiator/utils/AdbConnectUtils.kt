package com.lenovo.Initiator.utils

import android.util.Log
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

object AdbConnectUtils {
    private var TAG = AdbConnectUtils::class.java.simpleName
    /**
     * do command and get result
     */
    fun execRootCmd(cmd: String): String {
        var result = ""
        var dos: DataOutputStream? = null
        var dis: DataInputStream? = null

        try {
            val p = Runtime.getRuntime().exec("su")
            dos = DataOutputStream(p.outputStream)
            dis = DataInputStream(p.inputStream)

            Log.i(TAG, cmd)
            dos.writeBytes(cmd + "\n")
            dos.flush()
            dos.writeBytes("exit\n")
            dos.flush()
            while (dis.readLine() != null) {
                result += dis.readLine()
            }
            p.waitFor()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (dos != null) {
                try {
                    dos.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            if (dis != null) {
                try {
                    dis.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
        return result
    }
}