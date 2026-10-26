import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe
import kotlin.math.ceil
import kotlin.math.sqrt

object Quest01 {
    private fun parse(input: List<String>) = input[0].split(",").map(String::toInt)

    fun one(input: List<String>): Int {
        return parse(input).sumOf { n ->
            val s = ceil(sqrt(n.toDouble())).toInt()
            s * s - n
        }
    }

    fun two(input: List<String>): Int {
        return parse(input).sumOf { n ->
            val s = maxOf(3, (n + 7) / 4)
            4 * s - 4 - n
        }
    }

    fun three(input: List<String>): Int {
        return parse(input).sumOf { n ->
            var s = maxOf(3, ceil(sqrt(n.toDouble())).toInt())
            if (s % 2 == 0) s++
            val a = s * s - n - 1
            if (a >= 0) a else a + 4 * (s + 1)
        }
    }
}

val Quest01Test by testSuite {
    val quest = "01"

    with(Quest01) {
        test("one") {
            val sample = """
                3,11,12,16,24,25
            """.trimIndent().lines()
            one(sample) shouldBe 11

            val input = lines(quest, 1)
            one(input) shouldBe 1177
        }

        test("two") {
            val sample = """
                3,11,12,16,24,25
            """.trimIndent().lines()
            two(sample) shouldBe 9

            val input = lines(quest, 2)
            two(input) shouldBe 320
        }

        test("three") {
            val sample = """
                3,11,12,16,24,25
            """.trimIndent().lines()
            three(sample) shouldBe 61

            val input = lines(quest, 3)
            three(input) shouldBe 682762
        }
    }
}
