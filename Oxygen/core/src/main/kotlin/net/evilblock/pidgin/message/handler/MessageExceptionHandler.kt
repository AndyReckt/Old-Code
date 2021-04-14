/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.evilblock.pidgin.message.handler

class MessageExceptionHandler {

    fun onException(e: Exception) {
        println("Failed to send message")
        e.printStackTrace()
    }

}
