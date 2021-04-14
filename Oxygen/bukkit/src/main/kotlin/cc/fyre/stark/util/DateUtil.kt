/*
 * Copyright (c) 2020.
 * Created by YoloSanta
 * Created On 10/22/20, 1:23 AM
 */

package cc.fyre.stark.util

import java.math.BigDecimal
import java.text.DecimalFormat
import java.util.*
import java.util.regex.Pattern

object DateUtil {
    private val timePattern = Pattern.compile("(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?(?:([0-9]+)\\s*(?:s[a-z]*)?)?", 2)

    fun removeTimePattern(input: String): String {
        return timePattern.matcher(input).replaceFirst("").trim { it <= ' ' }
    }

    @Throws(Exception::class)
    fun parseDateDiff(time: String, future: Boolean): Long {
        val m = timePattern.matcher(time)
        var years = 0
        var months = 0
        var weeks = 0
        var days = 0
        var hours = 0
        var minutes = 0
        var seconds = 0
        var found = false

        while (m.find()) {
            if (m.group() != null && !m.group().isEmpty()) {
                for (c in 0 until m.groupCount()) {
                    if (m.group(c) != null && !m.group(c).isEmpty()) {
                        found = true
                        break
                    }
                }

                if (found) {
                    if (m.group(1) != null && !m.group(1).isEmpty()) {
                        years = Integer.parseInt(m.group(1))
                    }

                    if (m.group(2) != null && !m.group(2).isEmpty()) {
                        months = Integer.parseInt(m.group(2))
                    }

                    if (m.group(3) != null && !m.group(3).isEmpty()) {
                        weeks = Integer.parseInt(m.group(3))
                    }

                    if (m.group(4) != null && !m.group(4).isEmpty()) {
                        days = Integer.parseInt(m.group(4))
                    }

                    if (m.group(5) != null && !m.group(5).isEmpty()) {
                        hours = Integer.parseInt(m.group(5))
                    }

                    if (m.group(6) != null && !m.group(6).isEmpty()) {
                        minutes = Integer.parseInt(m.group(6))
                    }

                    if (m.group(7) != null && !m.group(7).isEmpty()) {
                        seconds = Integer.parseInt(m.group(7))
                    }
                    break
                }
            }
        }

        if (!found) {
            throw Exception("Illegal Date")
        } else {
            val var13 = GregorianCalendar()
            if (years > 0) {
                var13.add(1, years * if (future) 1 else -1)
            }

            if (months > 0) {
                var13.add(2, months * if (future) 1 else -1)
            }

            if (weeks > 0) {
                var13.add(3, weeks * if (future) 1 else -1)
            }

            if (days > 0) {
                var13.add(5, days * if (future) 1 else -1)
            }

            if (hours > 0) {
                var13.add(11, hours * if (future) 1 else -1)
            }

            if (minutes > 0) {
                var13.add(12, minutes * if (future) 1 else -1)
            }

            if (seconds > 0) {
                var13.add(13, seconds * if (future) 1 else -1)
            }

            val max = GregorianCalendar()
            max.add(1, 10)
            return if (var13.after(max)) max.timeInMillis else var13.timeInMillis
        }
    }

    internal fun dateDiff(type: Int, fromDate: Calendar, toDate: Calendar, future: Boolean): Int {
        var diff = 0

        var savedDate: Long
        savedDate = fromDate.timeInMillis
        while (future && !fromDate.after(toDate) || !future && !fromDate.before(toDate)) {
            savedDate = fromDate.timeInMillis
            fromDate.add(type, if (future) 1 else -1)
            ++diff
        }

        --diff
        fromDate.timeInMillis = savedDate
        return diff
    }

    fun formatDateDiff(date: Long): String {
        val c = GregorianCalendar()
        c.timeInMillis = date
        val now = GregorianCalendar()
        return formatDateDiff(now, c)
    }

    fun formatDateDiff(fromDate: Calendar, toDate: Calendar): String {
        var future = false
        if (toDate == fromDate) {
            return "now"
        } else {
            if (toDate.after(fromDate)) {
                future = true
            }

            val sb = StringBuilder()
            val types = intArrayOf(1, 2, 5, 11, 12, 13)
            val names = arrayOf("year", "years", "month", "months", "day", "days", "hour", "hours", "minute", "minutes", "second", "seconds")
            var accuracy = 0

            var i = 0
            while (i < types.size && accuracy <= 2) {
                val diff = dateDiff(types[i], fromDate, toDate, future)
                if (diff > 0) {
                    ++accuracy
                    sb.append(" ").append(diff).append(" ").append(names[i * 2 + (if (diff > 1) 1 else 0)])
                }
                ++i
            }

            return if (sb.length == 0) "now" else sb.toString().trim { it <= ' ' }
        }
    }

    fun formatSimplifiedDateDiff(date: Long): String {
        val c = GregorianCalendar()
        c.timeInMillis = date
        val now = GregorianCalendar()
        return formatSimplifiedDateDiff(now, c)
    }

    fun formatSimplifiedDateDiff(fromDate: Calendar, toDate: Calendar): String {
        var future = false
        if (toDate == fromDate) {
            return "now"
        } else {
            if (toDate.after(fromDate)) {
                future = true
            }

            val sb = StringBuilder()
            val types = intArrayOf(1, 2, 5, 11, 12, 13)
            val names = arrayOf("y", "y", "m", "m", "d", "d", "h", "h", "m", "m", "s", "s")
            var accuracy = 0

            var i = 0
            while (i < types.size && accuracy <= 2) {
                val diff = dateDiff(types[i], fromDate, toDate, future)
                if (diff > 0) {
                    ++accuracy
                    sb.append(" ").append(diff).append("").append(names[i * 2 + (if (diff > 1) 1 else 0)])
                }
                ++i
            }

            return if (sb.length == 0) "now" else sb.toString().trim { it <= ' ' }
        }
    }

    fun readableTime(time: Long): String {
        val SECOND: Short = 1000
        val MINUTE = 60 * SECOND
        val HOUR = 60 * MINUTE
        val DAY = 24 * HOUR
        var ms = time
        val text = StringBuilder("")
        if (time > DAY.toLong()) {
            text.append(time / DAY.toLong()).append(" days ")
            ms = time % DAY.toLong()
        }

        if (ms > HOUR.toLong()) {
            text.append(ms / HOUR.toLong()).append(" hours ")
            ms %= HOUR.toLong()
        }

        if (ms > MINUTE.toLong()) {
            text.append(ms / MINUTE.toLong()).append(" minutes ")
            ms %= MINUTE.toLong()
        }

        if (ms > SECOND.toLong()) {
            text.append(ms / SECOND.toLong()).append(" seconds ")
            val var10000 = ms % SECOND.toLong()
        }

        return text.toString()
    }

    fun readableTime(time: BigDecimal): String? {
        var time = time
        val text = ""

        if (time.toDouble() <= 60) {
            time = time.add(BigDecimal.valueOf(0.1))
            return text + " " + time + "s"
        } else if (time.toDouble() <= 3600) {
            val minutes = time.toInt() / 60
            val seconds = time.toInt() % 60
            val formatter = DecimalFormat("00")
            return text + " " + formatter.format(minutes.toLong()) + ":" + formatter.format(seconds.toLong()) + "m"
        }

        return null
    }
}