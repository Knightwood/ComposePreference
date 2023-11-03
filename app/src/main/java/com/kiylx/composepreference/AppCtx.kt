package com.kiylx.composepreference

import android.app.Application
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.plus


class AppCtx : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this
        scope = CoroutineScope(Dispatchers.IO) + SupervisorJob() + CoroutineName("GLOAB")
        //mmkv初始化
        MMKV.initialize(this)
    }

    companion object {
        lateinit var instance: AppCtx
        lateinit var scope: CoroutineScope
    }
}