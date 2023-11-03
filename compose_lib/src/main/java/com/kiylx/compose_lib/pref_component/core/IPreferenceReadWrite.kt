package com.kiylx.compose_lib.pref_component.core

import kotlinx.coroutines.flow.Flow

/**
 * 子类实现此接口以提供具体的每个偏好值的读写能力
 */
interface IPreferenceReadWrite<T> {

    fun read(): Flow<T>

    suspend fun write(data: T)

}