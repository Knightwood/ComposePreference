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