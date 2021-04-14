/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.nametag

import cc.fyre.stark.engine.scoreboard.ScoreboardTeamPacketMod
import java.util.*

class NametagInfo constructor(val name: String, val prefix: String, val suffix: String) {

    val teamAddPacket = ScoreboardTeamPacketMod(name, prefix, suffix, ArrayList(), 0)

    override fun equals(other: Any?): Boolean {
        if (other is NametagInfo) {
            val otherNametag = other as NametagInfo?
            return this.name == otherNametag!!.name && this.prefix == otherNametag.prefix && this.suffix == otherNametag.suffix
        }
        return false
    }

}
