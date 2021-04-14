/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.profile

import cc.fyre.stark.Stark
import cc.fyre.stark.core.pidgin.message.handler.IncomingMessageHandler
import cc.fyre.stark.core.pidgin.message.listener.MessageListener
import com.google.gson.JsonObject
import java.util.*

class ProfileMessageListeners : MessageListener {

    @IncomingMessageHandler("GRANT_UPDATE")
    fun onGrantUpdate(data: JsonObject) {
        val uuid = UUID.fromString(data.get("uuid").asString)
        val profile = Stark.instance.core.getProfileHandler().pullProfileUpdates(uuid)

        profile.apply()
    }

    @IncomingMessageHandler("PUNISHMENT_UPDATE")
    fun onPunishmentUpdate(data: JsonObject) {
        val uuid = UUID.fromString(data.get("uuid").asString)
        Stark.instance.core.getProfileHandler().pullProfileUpdates(uuid)
    }
}