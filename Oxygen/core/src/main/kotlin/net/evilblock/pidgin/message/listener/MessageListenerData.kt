/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.evilblock.pidgin.message.listener

import java.lang.reflect.Method

/**
 * A wrapper class that holds all the information needed to
 * identify and execute a message function.
 *
 */
data class MessageListenerData(val instance: Any, val method: Method, val id: String)
