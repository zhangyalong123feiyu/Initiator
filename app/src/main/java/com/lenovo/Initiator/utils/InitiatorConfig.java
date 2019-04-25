package com.lenovo.Initiator.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.lenovo.Initiator.protocol.ConfigBean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

/**
 * Json util.
 */
public class InitiatorConfig {

    private static final String TAG = "InitiatorConfig";

    private static final String FILE_NAME = "InitiatorServiceConfig.json";

    public static ConfigBean readConfig() {

        File dataFile = new File(Environment.getExternalStorageDirectory(), FILE_NAME);
        Log.d(TAG, "dataFile = " + dataFile);

        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();
        try {
            FileInputStream inputStream = new FileInputStream(dataFile);

            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    inputStream));

            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }

            bf.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        ConfigBean config = gson.fromJson(stringBuilder.toString(), ConfigBean.class);
        return config;
    }

    public static String readAssetJson(String fileName, Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        try {

            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static Object deserialize(String str, Type type) {

        Object object = new Gson().fromJson(str, type);
        return object;
    }
}
