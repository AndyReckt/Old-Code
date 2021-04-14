/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.core.mongo

import com.mongodb.MongoClient
import com.mongodb.MongoClientOptions
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import java.io.Closeable

class Mongo(private val dbName: String) : Closeable {

    lateinit var client: MongoClient
    lateinit var database: com.mongodb.client.MongoDatabase

    fun load(credentials: MongoCredentials) {
        client = if (credentials.shouldAuthenticate()) {
            val serverAddress = ServerAddress(credentials.host, credentials.port)
            val credential = MongoCredential.createCredential(credentials.username!!, "admin", credentials.password!!.toCharArray())

            MongoClient(serverAddress, credential, MongoClientOptions.builder().build())
        } else {
            MongoClient(credentials.host, credentials.port)
        }

        database = client.getDatabase(dbName)
    }

    override fun close() {
        client.close()
    }

}
