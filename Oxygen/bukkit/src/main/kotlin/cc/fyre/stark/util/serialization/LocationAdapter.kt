/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.util.serialization

import cc.fyre.stark.Stark
import org.bukkit.Location
import org.bukkit.craftbukkit.libs.com.google.gson.*
import java.lang.reflect.Type

class LocationAdapter : JsonDeserializer<Location>, JsonSerializer<Location> {
    override fun serialize(src: Location, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return toJson(src) as JsonElement
    }

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Location? {
        return fromJson(json)
    }

    companion object {
        fun toJson(location: Location?): JsonObject? {
            if (location == null) {
                return null
            }

            val jsonObject = JsonObject()
            jsonObject.addProperty("world", location.world.name)
            jsonObject.addProperty("x", location.x as Number)
            jsonObject.addProperty("y", location.y as Number)
            jsonObject.addProperty("z", location.z as Number)
            jsonObject.addProperty("yaw", location.yaw as Number)
            jsonObject.addProperty("pitch", location.pitch as Number)
            return jsonObject
        }

        fun fromJson(jsonElement: JsonElement?): Location? {
            if (jsonElement == null || !jsonElement.isJsonObject) {
                return null
            }

            val jsonObject = jsonElement.asJsonObject
            val world = Stark.instance.server.getWorld(jsonObject.get("world").asString)
            val x = jsonObject.get("x").asDouble
            val y = jsonObject.get("y").asDouble
            val z = jsonObject.get("z").asDouble
            val yaw = jsonObject.get("yaw").asFloat
            val pitch = jsonObject.get("pitch").asFloat

            return Location(world, x, y, z, yaw, pitch)
        }
    }
}