package com.lenovo.Initiator.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;

//
// This class has no use for future reservations.
//

public class RestartUtils {
    private static String TAG = "TAG";

    public static void killApps(String pid) {
        try {
            String s = "\n";
            String sErr = "\n";
            String cmd = "su -c kill -9 " + pid;
            Log.d(TAG, "cmd=" + cmd);

            Process p = Runtime.getRuntime().exec(cmd);

            InputStream is = p.getInputStream();
            InputStream ers = p.getErrorStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            DataInputStream ise = new DataInputStream(ers);
            String line = null;
            String error = null;
            while ((line = reader.readLine()) != null) {
                s += line + "\n";
            }
            Log.d(TAG, "s=" + s);
            while ((error = ise.readLine()) != null) {
                sErr += error + "\n";
            }
            Log.d(TAG, "sErr=" + sErr);
            int result = p.waitFor();
            Log.d(TAG, "result=" + result);

            is.close();
            ers.close();
            reader.close();
            ise.close();
            p.destroy();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void kill(Context context, String pack) {
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            Method method = Class.forName("android.app.ActivityManager").getMethod("forceStopPackage", String.class);
            method.invoke(am, "com.lenovo.billing");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * do command then get result
     */
    public static String execRootCmd(String cmd) {
        String result = "";
        DataOutputStream dos = null;
        DataInputStream dis = null;

        try {
            Process p = Runtime.getRuntime().exec("su");
            dos = new DataOutputStream(p.getOutputStream());
            dis = new DataInputStream(p.getInputStream());

            Log.i(TAG, cmd);
            dos.writeBytes(cmd + "\n");
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            String line = null;
            while ((line = dis.readLine()) != null) {
                Log.d("result", line);
                result += line;
            }
            p.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}

