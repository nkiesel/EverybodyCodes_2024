import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

object Quest01 {
    private fun parse(input: List<String>) = input[0]

    private val potions = mapOf('x' to 0, 'A' to 0, 'B' to 1, 'C' to 3, 'D' to 5)
    private fun isX(c: Char) = c == 'x'

    fun one(input: List<String>) = parse(input).sumOf { potions[it]!! }

    fun two(input: List<String>) = parse(input).chunked(2).sumOf { group ->
        group.sumOf { potions[it]!! } + if (group.none(::isX)) 2 else 0
    }

    fun three(input: List<String>) = parse(input).chunked(3).sumOf { group ->
        group.sumOf { potions[it]!! } + when (group.count(::isX)) {
            0 -> 6
            1 -> 2
            else -> 0
        }
    }
}

val Quest01Test by testSuite {
    val quest = "01"

    with(Quest01) {
        test("one") {
            val sample = """
                ABBAC
            """.trimIndent().lines()
            one(sample) shouldBe 5

            val input = lines(quest, 1)
            one(input) shouldBe 1329
        }

        test("two") {
            val sample = """
                AxBCDDCAxD
            """.trimIndent().lines()
            two(sample) shouldBe 28

            val input = lines(quest, 2)
            two(input) shouldBe 5646
        }

        test("three") {
            val sample = """
                xBxAAABCDxCC
            """.trimIndent().lines()
            three(sample) shouldBe 30

            val input = lines(quest, 3)
            three(input) shouldBe 28209
        }
    }
}
