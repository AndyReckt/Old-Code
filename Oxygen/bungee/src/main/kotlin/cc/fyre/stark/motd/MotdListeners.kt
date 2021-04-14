/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.motd

import cc.fyre.stark.Stark
import cc.fyre.stark.core.util.TimeUtils
import cc.fyre.stark.util.ChatUtil
import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.ComponentBuilder
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.event.ProxyPingEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

class MotdListeners : Listener {

    @EventHandler
    fun onProxyPingEvent(event: ProxyPingEvent) {
        val activeMotd = Stark.instance.motdHandler.active

        if (activeMotd != null) {
            var textToTranslate = activeMotd.motd

            if (activeMotd.countdown != null) {
                val current = System.currentTimeMillis()
                val remaining = activeMotd.countdown!! - current

                textToTranslate = if (remaining < 0) {
                    activeMotd.countdownFinishMotd!!
                } else {
                    textToTranslate.replace("%countdown%", TimeUtils.formatIntoShortString((remaining / 1000).toInt()), true)
                }
            }

            val description = ChatUtil.color(textToTranslate)
            val descriptionBuilder = ComponentBuilder("")

            description.split(ChatColor.COLOR_CHAR.toString() + "r").forEach {
                descriptionBuilder.append(TextComponent.fromLegacyText(it))
                descriptionBuilder.append(TextComponent(""))
                descriptionBuilder.bold(false)
                descriptionBuilder.italic(false)
                descriptionBuilder.obfuscated(false)
                descriptionBuilder.strikethrough(false)
            }

            val serverPing = event.response
            serverPing.descriptionComponent = ChatUtil.compile(descriptionBuilder.create())
            event.response = serverPing
        }
    }
}