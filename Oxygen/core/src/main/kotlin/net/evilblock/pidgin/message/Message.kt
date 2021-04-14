/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.evilblock.pidgin.message

class Message(var id: String, var data: Map<String, Any>) {

    constructor(id: String) : this(id, hashMapOf())

}
