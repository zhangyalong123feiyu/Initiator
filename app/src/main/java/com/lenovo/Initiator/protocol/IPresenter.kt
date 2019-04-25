package com.lenovo.Initiator.protocol

interface IPresenter {
    fun onGetDownloadSucess(msg: String)
    fun onGetDownloadFailed(erro: String)
}