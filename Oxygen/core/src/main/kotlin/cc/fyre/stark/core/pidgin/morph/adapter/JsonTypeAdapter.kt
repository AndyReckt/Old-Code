/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.core.pidgin.morph.adapter

import com.google.gson.JsonElement

interface JsonTypeAdapter<T : Any> {

    fun toJson(src: T): JsonElement

    fun toType(element: JsonElement): T

}
