/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.engine.command.data.parameter

import cc.fyre.stark.engine.command.data.Data

data class ParameterData(val name: String,
                         val defaultValue: String,
                         val type: Class<*>,
                         val wildcard: Boolean,
                         val methodIndex: Int,
                         val tabCompleteFlags: Set<String>,
                         val parameterType: Class<*>?) : Data