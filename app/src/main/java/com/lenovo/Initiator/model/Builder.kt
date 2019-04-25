package com.lenovo.Initiator.model

class Builder {
    companion object {
        private val TAG = Builder::class.java.simpleName
    }

    fun buildDeviceInitiator(): DeviceInitiator {
        return DeviceInitiator()
    }

}