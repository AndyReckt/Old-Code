/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.util

import cc.fyre.stark.Stark
import com.mongodb.BasicDBList
import java.util.*

object UUIDUtils {


    @JvmStatic
    fun formatPretty(uuid: UUID): String {
        return Stark.instance.core.uuidCache.name(uuid)/* + " [" + uuid + "]"*/ //why do we need this?????
    }

    @JvmStatic
    fun uuidsToStrings(toConvert: Collection<UUID>?): BasicDBList {
        if (toConvert == null || toConvert.isEmpty()) {
            return BasicDBList()
        }

        val dbList = BasicDBList()
        for (uuid in toConvert) {
            dbList.add(uuid.toString())
        }

        return dbList
    }
}
