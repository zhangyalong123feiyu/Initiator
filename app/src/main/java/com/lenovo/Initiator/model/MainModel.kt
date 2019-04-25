package com.lenovo.Initiator.model

import android.content.Context
import android.util.Log
import com.lenovo.Initiator.protocol.Config
import com.lenovo.Initiator.protocol.DownloadApi
import com.lenovo.Initiator.protocol.IPresenter
import com.lenovo.Initiator.utils.AdbConnectUtils
import com.lenovo.Initiator.utils.DownloadUtil
import com.lenovo.Initiator.utils.InitiatorConfig
import com.lenovo.Initiator.utils.RetrofitUtils
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainModel(iPresenter: IPresenter, context: Context) {
    private val iPresenter: IPresenter
    private val context: Context

    init {
        this.iPresenter = iPresenter
        this.context = context
        val configBean = InitiatorConfig.readConfig()
        Config.override(configBean)
        val buider = Builder()
        val deviceInitiator = buider.buildDeviceInitiator()
        deviceInitiator.startApps()
    }

    fun downloadApk(url: String) {
        var retrofit = RetrofitUtils.createRequest(DownloadApi::class.java)

        retrofit.downloadApk(url).enqueue(object : Callback<ResponseBody> {

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                iPresenter.onGetDownloadSucess("下载成功")
                val downloadUtil = DownloadUtil(context)
                val isToSdcard = downloadUtil.writeTosDcard(context, response.body()!!)
                if (isToSdcard) {
                    iPresenter.onGetDownloadSucess("write sucess")
                } else {
                    iPresenter.onGetDownloadFailed("write fialed")
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                iPresenter.onGetDownloadFailed(t.message!!)
            }
        })
    }

    //
    // Init adb to connect a device.
    //

    fun initAdb(): String {
        AdbConnectUtils.execRootCmd("setprop service.adb.tcp.port 5555")
        AdbConnectUtils.execRootCmd("stop adbd")
        var result = AdbConnectUtils.execRootCmd("start adbd")
        Log.i("TAG", "init adb")
        return result
    }
}
