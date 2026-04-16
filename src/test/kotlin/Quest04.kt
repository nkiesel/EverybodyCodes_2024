import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe
import kotlin.math.abs

object Quest04 {
    private fun parse(input: List<String>) = input.map { it.toInt() }

    fun one(input: List<String>): Int {
        val nails = parse(input)
        val shortest = nails.min()
        return nails.sumOf { it - shortest }
    }

    fun two(input: List<String>) = one(input)

    fun three(input: List<String>): Int {
        val nails = parse(input)
        return nails.distinct().minOf { target -> nails.sumOf { abs(it - target) } }
    }
}

val Quest04Test by testSuite {
    val quest = "04"

    with(Quest04) {
        test("one") {
            val sample = """
                3
                4
                7
                8
            """.trimIndent().lines()
            one(sample) shouldBe 10

            val input = lines(quest, 1)
            one(input) shouldBe 67
        }

        test("two") {
            val input = lines(quest, 2)
            two(input) shouldBe 833814
        }

        test("three") {
            val sample = """
                2
                4
                5
                6
                8
            """.trimIndent().lines()
            three(sample) shouldBe 8

            val input = lines(quest, 3)
            three(input) shouldBe 122634414
        }
    }
}
