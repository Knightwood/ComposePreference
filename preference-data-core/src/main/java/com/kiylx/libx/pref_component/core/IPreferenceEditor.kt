/*
 * Copyright 2024 [KnightWood]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.kiylx.libx.pref_component.core

import kotlinx.coroutines.flow.Flow

/**
 * 子类实现此接口以提供具体的每个偏好值的读写能力
 */
interface IPreferenceEditor<T> {

    fun flow(): Flow<T>

    fun readValue(): T

    suspend fun readValueAsync(): T = readValue()

    suspend fun write(data: T)

}
//兼容旧名称
typealias IPreferenceReadWrite<T> = IPreferenceEditor<T>