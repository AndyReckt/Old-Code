/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.util

import org.bukkit.Effect
import org.bukkit.Location

/**
 * Created by DaddyDombo daddydombo@gmail.com on 2/2/2020.
 */
data class ParticleMeta(val location: Location, val effect: Effect) {

    constructor(location: Location,
                effect: Effect,
                offsetX: Float,
                offsetY: Float,
                offsetZ: Float,
                speed: Float,
                amount: Int) : this(location, effect) {
        this.offsetX = offsetX
        this.offsetY = offsetY
        this.offsetZ = offsetZ
        this.speed = speed
        this.amount = amount
    }

    var offsetX = 0.0F
    var offsetY = 0.0F
    var offsetZ = 0.0F
    var speed = 1.0F
    var amount = 1

}