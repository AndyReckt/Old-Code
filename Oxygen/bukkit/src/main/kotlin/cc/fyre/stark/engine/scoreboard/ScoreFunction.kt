/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.scoreboard

import cc.fyre.stark.core.util.TimeUtils

interface ScoreFunction<T> {

    fun apply(score: T): String

    companion object {
        fun TIME_FANCY(value: Float): String {
            return if (value >= 60.0f) {
                TimeUtils.formatIntoMMSS(value.toInt())
            } else {
                (Math.round(10.0 * value) / 10.0).toString() + "s"
            }
        }

        fun TIME_SIMPLE(value: Int): String {
            return TimeUtils.formatIntoMMSS(value)
        }
    }
}