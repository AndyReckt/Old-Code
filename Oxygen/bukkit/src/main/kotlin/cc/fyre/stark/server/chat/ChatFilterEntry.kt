/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.server.chat

import java.util.regex.Pattern

class ChatFilterEntry(val id: String, regex: String) {

    val pattern = Pattern.compile(regex)

}