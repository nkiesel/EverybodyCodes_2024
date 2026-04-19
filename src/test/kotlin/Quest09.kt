import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

object Quest09 {
    private fun parse(input: List<String>) = input.map { it.toInt() }

    fun one(input: List<String>): Int {
        val balls = parse(input)
        val stamps = listOf(1, 3, 5, 10).sortedDescending()
        return balls.sumOf { ball ->
            var num = ball
            stamps.sumOf {
                val n = num / it
                num -= n * it
                n
            }
        }
    }

    fun two(input: List<String>): Int {
        val balls = parse(input)
        val allStamps = setOf(1, 3, 5, 10, 15, 16, 20, 24, 25, 30)
        val best = mutableMapOf<Int, Int>()
        fun mc(n: Int): Int {
            if (n < 0) return Int.MAX_VALUE - 10000
            if (n in allStamps) return 1
            if (n in best) return best.getValue(n)
            val b = allStamps.minOf { s -> mc(n - s) + 1 }
            best[n] = b
            return b
        }
        return balls.sumOf { ball -> mc(ball) }
    }

    fun three(input: List<String>): Int {
        return 0
    }
}

val Quest09Test by testSuite {
    val quest = "09"

    with(Quest09) {
        test("one") {
            val sample = """
                2
                4
                7
                16
            """.trimIndent().lines()
            one(sample) shouldBe 10

            val input = lines(quest, 1)
            one(input) shouldBe 13502
        }

        test("two") {
            val sample = """
                33
                41
                55
                99
            """.trimIndent().lines()
            two(sample) shouldBe 10

            val input = lines(quest, 2)
            two(input) shouldBe 5080
        }

        test("three") {
            val sample = """
            """.trimIndent().lines()
            three(sample) shouldBe 0

//            val input = lines(quest, 3)
//            three(input) shouldBe 0
        }
    }
}
