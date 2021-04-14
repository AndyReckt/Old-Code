/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package net.evilblock.pidgin

import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import net.evilblock.pidgin.message.Message
import net.evilblock.pidgin.message.handler.IncomingMessageHandler
import net.evilblock.pidgin.message.handler.MessageExceptionHandler
import net.evilblock.pidgin.message.listener.MessageListener
import net.evilblock.pidgin.message.listener.MessageListenerData
import net.evilblock.pidgin.morph.JsonMorph
import redis.clients.jedis.JedisPool
import redis.clients.jedis.JedisPubSub
import java.util.*
import java.util.concurrent.ForkJoinPool

/**
 * A Jedis Pub/Sub implementation.
 */
class Pidgin(private val channel: String, private val jedisPool: JedisPool, private val options: PidginOptions = PidginOptions()) {

    private var jedisPubSub: JedisPubSub? = null
    private val messageListeners: MutableList<MessageListenerData> = ArrayList()

    init {
        setupPubSub()
    }

    @JvmOverloads
    fun sendMessage(message: Message, exceptionHandler: MessageExceptionHandler? = null) {
        try {
            val jsonObject = JsonObject()
            jsonObject.addProperty("messageId", message.id)
            jsonObject.add("messageData", morph.fromObject(message.data))

            jedisPool.resource.use { jedis -> jedis.publish(channel, jsonObject.toString()) }
        } catch (e: Exception) {
            exceptionHandler?.onException(e)
        }
    }

    fun registerListener(messageListener: MessageListener) {
        for (method in messageListener::class.java.declaredMethods) {
            if (method.getDeclaredAnnotation(IncomingMessageHandler::class.java) != null && method.parameters.isNotEmpty()) {
                if (!JsonObject::class.java.isAssignableFrom(method.parameters[0].type)) {
                    throw IllegalStateException("First parameter should be of JsonObject type")
                }

                val messageId = method.getDeclaredAnnotation(IncomingMessageHandler::class.java).id

                messageListeners.add(MessageListenerData(messageListener, method, messageId))
            }
        }
    }

    private fun setupPubSub() {
        jedisPubSub = object : JedisPubSub() {
            override fun onMessage(channel: String, message: String) {
                if (channel.equals(this@Pidgin.channel, ignoreCase = true)) {
                    try {
                        val messagePayload = parser.parse(message).asJsonObject
                        val messageId = messagePayload.get("messageId").asString
                        val messageData = messagePayload.get("messageData").asJsonObject

                        for (data in messageListeners) {
                            if (data.id == messageId) {
                                data.method.invoke(data.instance, messageData)
                            }
                        }
                    } catch (e: JsonParseException) {
                        println("[Pidgin] Expected JSON message but could not parse message")
                        e.printStackTrace()
                    } catch (e: Exception) {
                        println("[Pidgin] Failed to handle message")
                        e.printStackTrace()
                    }
                }
            }
        }

        if (options.async) {
            ForkJoinPool.commonPool().execute { jedisPool.resource.use { jedis -> jedis.subscribe(jedisPubSub!!, channel) } }
        } else {
            jedisPool.resource.use { jedis -> jedis.subscribe(jedisPubSub!!, channel) }
        }
    }

    companion object {

        val parser = JsonParser()
        val morph = JsonMorph()

    }

}