package ethiopia.covid.android

import ethiopia.covid.android.util.Utils.formatNumber
import ethiopia.covid.android.util.Utils.getTimeGap
import org.junit.Assert
import org.junit.Test
import java.util.*
import java.util.concurrent.TimeUnit

class UtilsUnitTest {
    @Test
    fun number_correctly_formatted() {
        val numbers = longArrayOf(1000, 102344, 22, 494, 1000000000)
        val formatted = arrayOf("1,000", "102,344", "22", "494", "1,000,000,000")
        for (i in numbers.indices) {
            Assert.assertEquals("Testing " + numbers[i], formatted[i], formatNumber(numbers[i]))
        }
    }

    @Test
    fun time_gap_is_presented_correctly() {
        val numbers = longArrayOf(
                Date().time - (TimeUnit.HOURS.toMillis(2)),
                Date().time - (TimeUnit.MINUTES.toMillis(12)),
                Date().time - (TimeUnit.SECONDS.toMillis(1)),
                Date().time - (TimeUnit.MILLISECONDS.toMillis(20)),
                Date().time - (TimeUnit.DAYS.toMillis(41))
        )

        val formatted = arrayOf("2h ago" , "12m ago" , "just now" , "just now" , "6w ago")
        for (i in numbers.indices) {
            Assert.assertEquals("Testing " + formatted[i], formatted[i], getTimeGap(numbers[i]))
        }
    }
}