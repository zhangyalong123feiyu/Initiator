package com.lenovo.Initiator.presenter;

import android.content.Context;

import com.lenovo.Initiator.model.MainModel;
import com.lenovo.Initiator.protocol.Config;
import com.lenovo.Initiator.protocol.IPresenter;
import com.lenovo.Initiator.protocol.IView;

import org.jetbrains.annotations.NotNull;

public class MainPresenter implements IPresenter {
    private IView iView;
    private MainModel mainModel;

    public MainPresenter(IView iView, Context context) {
        this.iView = iView;
        mainModel = new MainModel(this, context);

        //
        //  Reading configuration files, whether to initialize the adb
        //

        if(!Config.foreCloseAccess){
            mainModel.initAdb();
        }
    }

    public void downloadApk(String url, final Context context) {
        mainModel.downloadApk(url);
    }

    @Override
    public void onGetDownloadSucess(@NotNull String msg) {
        iView.onGetDownloadSucess(msg);
    }

    @Override
    public void onGetDownloadFailed(@NotNull String erro) {
        iView.onGetDownloadFailed(erro);
    }
}
