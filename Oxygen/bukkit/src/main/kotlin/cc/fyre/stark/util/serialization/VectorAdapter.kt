/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.util.serialization

import org.bukkit.craftbukkit.libs.com.google.gson.*
import org.bukkit.util.Vector
import java.lang.reflect.Type

class VectorAdapter : JsonDeserializer<Vector>, JsonSerializer<Vector> {
    @Throws(JsonParseException::class)
    override fun deserialize(src: JsonElement, type: Type, context: JsonDeserializationContext): Vector? {
        return fromJson(src)
    }

    override fun serialize(src: Vector, type: Type, context: JsonSerializationContext): JsonElement {
        return toJson(src) as JsonElement
    }

    companion object {

        fun toJson(src: Vector?): JsonObject? {
            if (src == null) {
                return null
            }
            val `object` = JsonObject()
            `object`.addProperty("x", src.x as Number)
            `object`.addProperty("y", src.y as Number)
            `object`.addProperty("z", src.z as Number)
            return `object`
        }

        fun fromJson(src: JsonElement?): Vector? {
            if (src == null || !src.isJsonObject) {
                return null
            }
            val json = src.asJsonObject
            val x = json.get("x").asDouble
            val y = json.get("y").asDouble
            val z = json.get("z").asDouble
            return Vector(x, y, z)
        }
    }
}
