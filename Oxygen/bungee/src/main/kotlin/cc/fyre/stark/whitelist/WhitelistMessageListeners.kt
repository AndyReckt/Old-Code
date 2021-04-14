/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.whitelist

import cc.fyre.stark.Stark
import cc.fyre.stark.core.pidgin.message.handler.IncomingMessageHandler
import cc.fyre.stark.core.pidgin.message.listener.MessageListener
import cc.fyre.stark.core.whitelist.WhitelistType
import com.google.gson.JsonObject
import net.md_5.bungee.api.chat.TextComponent
import java.util.*
import java.util.concurrent.TimeUnit

class WhitelistMessageListeners : MessageListener {

    @IncomingMessageHandler("WHITELIST_UPDATE")
    fun onWhitelistUpdate(data: JsonObject) {
        Stark.instance.core.whitelist.setMode(WhitelistType.valueOf(data.get("mode").asString), false)

        for (player in Stark.instance.proxy.players) {
            val hasWhitelist = Stark.instance.core.whitelist.getWhitelist(player.uniqueId).isAboveOrEqual(Stark.instance.core.whitelist.modeType)
            val hasPermission = player.hasPermission(Stark.instance.core.whitelist.modeType.getPermission())

            if (!hasWhitelist && !hasPermission) {
                Stark.instance.proxy.scheduler.schedule(Stark.instance, {
                    player.disconnect(TextComponent.fromLegacyText(Stark.instance.core.whitelist.modeType.disallowMessage).component1())
                }, 1L, TimeUnit.MILLISECONDS)
            }
        }
    }

    @IncomingMessageHandler("VERIFIED_UPDATE")
    fun onVerifiedUpdate(data: JsonObject) {
        val status = data.get("status").asBoolean
        val playerUuid = UUID.fromString(data.get("playerUuid").asString)

        if (status) {
            Stark.instance.core.whitelist.verified.add(playerUuid)
        } else {
            Stark.instance.core.whitelist.verified.remove(playerUuid)
        }
    }
}