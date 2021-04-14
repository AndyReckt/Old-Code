/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.motd

import cc.fyre.stark.Stark

class MotdHandler {

    var active: MotdState? = null
    val states: MutableMap<String, MotdState> = hashMapOf()

    fun loadStates() {
        states.clear()

        val config = Stark.instance.configuration

        if (config.contains("ServerList.MOTDStates")) {
            for (key in config.getSection("ServerList.MOTDStates").keys) {
                val state = MotdState(key, config.getString("ServerList.MOTDStates.$key.MOTD"))

                if (config.contains("ServerList.MOTDStates.$key.Countdown")) {
                    state.countdown = config.getLong("ServerList.MOTDStates.$key.Countdown")
                    state.countdownFinishMotd = config.getString("ServerList.MOTDStates.$key.CountdownFinishMOTD")
                }

                states[state.name] = state
            }
        }

        if (config.contains("ServerList.ActiveMOTDState")) {
            if (states.containsKey(config.getString("ServerList.ActiveMOTDState"))) {
                active = states[config.getString("ServerList.ActiveMOTDState")]!!
            }
        }

        if (active == null) {
            Stark.instance.logger.warning("There's no active MOTD state")
        }
    }
}