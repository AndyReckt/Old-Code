/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.evilblock.pidgin.morph.adapter.impl

import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import net.evilblock.pidgin.morph.adapter.JsonTypeAdapter
import java.util.*

class UUIDTypeAdapter : JsonTypeAdapter<UUID> {

    override fun toJson(src: UUID): JsonElement {
        return JsonPrimitive(src.toString())
    }

    override fun toType(element: JsonElement): UUID {
        return UUID.fromString(element.asString)
    }

}
