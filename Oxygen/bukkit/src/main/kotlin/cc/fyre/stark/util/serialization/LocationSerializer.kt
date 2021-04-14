/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.util.serialization

import cc.fyre.stark.Stark
import com.mongodb.BasicDBObject
import org.bukkit.Location

object LocationSerializer {

    @JvmStatic
    fun serialize(location: Location?): BasicDBObject {
        if (location == null) {
            return BasicDBObject()
        }

        val dbObject = BasicDBObject()
        dbObject["world"] = location.world.name
        dbObject["x"] = location.x
        dbObject["y"] = location.y
        dbObject["z"] = location.z
        dbObject.append("yaw", location.yaw)
        dbObject.append("pitch", location.pitch)
        return dbObject
    }

    @JvmStatic
    fun deserialize(dbObject: BasicDBObject?): Location? {
        if (dbObject == null || dbObject.isEmpty()) {
            return null
        }

        val world = Stark.instance.server.getWorld(dbObject.getString("world"))
        val x = dbObject.getDouble("x")
        val y = dbObject.getDouble("y")
        val z = dbObject.getDouble("z")
        val yaw = dbObject.getInt("yaw")
        val pitch = dbObject.getInt("pitch")
        return Location(world, x, y, z, yaw.toFloat(), pitch.toFloat())
    }

}