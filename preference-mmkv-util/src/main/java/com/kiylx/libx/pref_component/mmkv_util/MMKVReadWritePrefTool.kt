package com.kiylx.libx.pref_component.mmkv_util

import com.kiylx.libx.pref_component.core.IPreferenceReadWrite
import com.tencent.mmkv.MMKV
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

/**
 * 提供偏好值的读写，MMKV实现功能版本
 */
class MMKVReadWritePrefTool<T : Any>(
    val kv: MMKV,
    val keyName: String,
    val defaultValue: T,
) : IPreferenceReadWrite<T> {
    val TAG="mmkv_tool"

    private val flow: MutableSharedFlow<T> = MutableSharedFlow<T>(1)
    var readWrite:MmkvUtil<T> =  (when (defaultValue) {
        is Int -> {
            kv.intRW(keyName, defaultValue)
        }

        is Boolean -> {
            kv.boolRW(keyName, defaultValue)
        }

        is String -> {
            kv.strRW(keyName, defaultValue)
        }

        is Double -> {
            kv.doubleRW(keyName, defaultValue)
        }

        is Float -> {
            kv.floatRW(keyName, defaultValue)
        }

        is Long -> {
            kv.longRW(keyName, defaultValue)
        }

        else -> {
            throw IllegalArgumentException("not support")
        }
    }) as MmkvUtil<T>

    init {
        flow.tryEmit(readWrite.read())
    }

    override fun read(): Flow<T> {
        return flow
    }

    override suspend fun write(data: T) {
        readWrite.write(data)
        flow.emit(data)
    }
}