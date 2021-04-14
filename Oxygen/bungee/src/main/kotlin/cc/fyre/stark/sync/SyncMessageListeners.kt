/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.sync

import cc.fyre.stark.Stark
import cc.fyre.stark.core.pidgin.message.handler.IncomingMessageHandler
import cc.fyre.stark.core.pidgin.message.listener.MessageListener
import com.google.gson.JsonObject

class SyncMessageListeners : MessageListener {

    @IncomingMessageHandler("PROXY_HEARTBEAT")
    fun onProxyHeartbeatMessage(json: JsonObject) {
        val proxyId = json["proxy"].asString

        if (Stark.instance.proxy.name !== proxyId) {
            val syncedProxy = Stark.instance.syncHandler.proxies[proxyId] ?: SyncedProxy(proxyId)
            syncedProxy.online = json["online"].asInt
            syncedProxy.lastUpdate = System.currentTimeMillis()

            Stark.instance.syncHandler.proxies[syncedProxy.proxyId] = syncedProxy
        }
    }
}