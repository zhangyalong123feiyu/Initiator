package com.lenovo.Initiator.utils

import android.app.Notification
import android.content.Context
import android.util.Log
import okhttp3.ResponseBody
import java.io.*
import android.support.v4.app.NotificationCompat
import android.graphics.BitmapFactory
import android.app.NotificationManager
import android.graphics.Color
import com.lenovo.Initiator.R


class DownloadUtil(context: Context) {
    private var context: Context

    init {
        this.context = context
    }

    fun writeTosDcard(context: Context, body: ResponseBody): Boolean {
        try {
            var futureStudioIconFile = File(context.getExternalFilesDir(null).toString() + File.separator + "Future Studio Icon.png");
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null

            try {
                var fileReader = ByteArray(4096);

                var fileSize: Long = body.contentLength();
                var fileSizeDownloaded: Long = 0;

                inputStream = body.byteStream();
                outputStream = FileOutputStream(futureStudioIconFile);

                while (true) {
                    var read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;
                    var progress: String = (100 * fileSizeDownloaded / fileSize).toString()
                    showtNotification(progress)
                    Log.i("TAG", "file download: " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();

                return true;
            } catch (e: IOException) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (e: IOException) {
            return false;
        }
    }

    //
    // init notification.
    //

    fun showtNotification(progress: String) {
        var notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        var builder = NotificationCompat.Builder(context)
        builder.setContentTitle("正在更新...")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher_round))
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(false)
                .setContentText("下载进度:" + progress)
                .setColor(Color.argb(0, 100, 55, 88))
                .setProgress(100, progress.toInt(), false)
        var notification = builder.build()

        notificationManager.notify(1, notification)
    }


}