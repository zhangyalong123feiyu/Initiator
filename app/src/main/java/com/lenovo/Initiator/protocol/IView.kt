package com.lenovo.Initiator.protocol

interface IView {
    fun onGetDownloadSucess(msg: String)
    fun onGetDownloadFailed(erro: String)
}