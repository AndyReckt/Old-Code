/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.core.fetcher

import java.util.*

/**
 * Created by DaddyDombo daddydombo@gmail.com on 5/1/2020.
 */
interface HCFDataFetcher {

    fun getLives(uuid: UUID): Int = 0
    fun getDeathBan(uuid: UUID): Long = 0
    fun getPlaytime(uuid: UUID): Long = 0
    fun getBalance(uuid: UUID): Double = 0.0

}