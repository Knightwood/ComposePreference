package com.kiylx.libx.pref_component.core

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

/**
 * 默认的偏好值存储工具，其实他根本不会存储偏好值。
 */
class DefaultPreferenceHolder internal constructor(): PreferenceHolder() {
    override fun <T : Any> getSingleDataEditor(
        keyName: String,
        defaultValue: T
    ): IPreferenceEditor<T> {
        return hashMap[keyName]?.let {
            it as IPreferenceEditor<T>
        } ?: let {
            val tmp = FakeEditor(keyName, defaultValue)
            hashMap[keyName] = tmp
            tmp
        }
    }
    
    companion object{
        @Volatile
        var ps: DefaultPreferenceHolder? = null
        fun instance(
        ): DefaultPreferenceHolder {
            return ps ?: synchronized(this) {
                ps ?: DefaultPreferenceHolder().also { ps = it }
            }
        }
    }
}

class FakeEditor<T : Any>(
    val keyName: String,
    val defaultValue: T,
) : IPreferenceEditor<T> {
    private val stateFlow: MutableStateFlow<T> = MutableStateFlow(defaultValue)

    override fun flow(): Flow<T> {
        return stateFlow
    }

    override fun readValue(): T {
        return stateFlow.value
    }

    override suspend fun write(data: T) {
        this.stateFlow.emit(data)
    }
}