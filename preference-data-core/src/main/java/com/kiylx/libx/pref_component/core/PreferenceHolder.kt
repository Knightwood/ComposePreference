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

import kotlinx.coroutines.flow.MutableStateFlow

abstract class PreferenceHolder {
    //记录每个偏好值的key与其对应的编辑工具
    val hashMap: HashMap<String, Any> = hashMapOf()

    //记录每个key对应的enable状态
    val dependenceTree: HashMap<String, DependenceNode> = hashMapOf()

    init {
        //放入默认的公共依赖的根节点
        dependenceTree[DependenceNode.rootName] = DependenceNode(
            enable = true,
            keyName = DependenceNode.rootName
        )
    }

    /**
     * 获取编辑单个偏好值的读写工具，其持有某个key对应的偏好值
     *
     * @param keyName String
     * @param defaultValue T
     * @return IPreferenceReadWrite<T>
     */
    abstract fun <T : Any> getSingleDataEditor(
        keyName: String,
        defaultValue: T,
    ): IPreferenceEditor<T>

    /**
     * 将注册者自身(preference compose function)的状态记录下来，并返回注册者依赖的节点状态
     * @param currentKey 注册者自身的key
     * @param currentState 注册者自身的状态
     * @param targetKey 注册者要依赖于哪个节点的key，如果为null，则依赖于根节点状态
     * @return 返回依赖的节点的状态,若targetKey为null，返回自身节点状态
     */
    fun getDependence(
        currentKey: String,
        currentState: Boolean,
        targetKey: String? = null
    ): DependenceNode {
        if (!dependenceTree.contains(currentKey)) {
            val node = DependenceNode(currentState, currentKey)
            dependenceTree.putIfAbsent(currentKey, node)
        }
        return targetKey?.let {
            dependenceTree[it]
        } ?: dependenceTree[currentKey]!!
    }

    /**
     * 注册并返回key的状态
     */
    fun registerDependence(
        key: String,
        state: Boolean,
    ): DependenceNode {
        if (!dependenceTree.contains(key)) {
            val node = DependenceNode(state, key)
            dependenceTree.putIfAbsent(key, node)
        }
        return dependenceTree[key]!!
    }

    /**
     * 获取某个key对应的状态
     */
    fun getDependence(
        key: String
    ): DependenceNode? {
        return dependenceTree[key]
    }

    /**
     * 获取某个key对应的状态,如不存在，返回默认启用状态
     */
    fun getDependenceNotEmpty(
        key: String?,
        enable: Boolean=true,
    ): DependenceNode {
        return dependenceTree[key] ?: DependenceNode(enable, "")
    }


}

class DependenceNode(
    enable: Boolean,
    val keyName: String,
) {
    val enableStateFlow: MutableStateFlow<Boolean> = MutableStateFlow(enable)

//    val enableState = mutableStateOf(enable)

    companion object {
        const val rootName = "Pref_Dependence_Node_Root"
    }
}