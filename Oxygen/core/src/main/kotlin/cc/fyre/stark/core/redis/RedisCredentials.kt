/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.core.redis

data class RedisCredentials(
        var host: String = "172.18.0.1",
        var port: Int = 6379,
        var password: String? = null,
        var dbId: Int = 0) {

    fun shouldAuthenticate(): Boolean {
        return password != null && password!!.isNotEmpty() && password!!.isNotBlank()
    }

    class Builder {
        val credentials: RedisCredentials = RedisCredentials()

        fun host(host: String): Builder {
            credentials.host = host
            return this
        }

        fun port(port: Int): Builder {
            credentials.port = port
            return this
        }

        fun password(password: String): Builder {
            credentials.password = password
            return this
        }

        fun dbId(dbId: Int): Builder {
            credentials.dbId = dbId
            return this
        }

        fun build(): RedisCredentials {
            return credentials
        }
    }

}
