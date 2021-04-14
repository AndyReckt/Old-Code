/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.server.chat

import java.util.*
import kotlin.collections.HashMap

class SlowedChatSession(val issuer: String, val duration: Long) {
    val players = HashMap<UUID, Long>()
}