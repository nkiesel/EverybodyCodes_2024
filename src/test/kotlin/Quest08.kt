import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe
import kotlin.math.sqrt
import kotlin.math.pow
import kotlin.math.ceil

object Quest08 {
    private fun parse(input: List<String>) = input[0].toLong()

    fun one(input: List<String>): Long {
        val num = parse(input)
        val s = sqrt(num.toDouble())
        val bottomLength = 1 + 2 * s.toLong()
        val additional = ceil(s).pow(2).toLong() - num
        return bottomLength * additional
    }

    fun two(input: List<String>): Int {
        return 0
    }

    fun three(input: List<String>): Int {
        return 0
    }
}

val Quest08Test by testSuite {
    val quest = "08"

    with(Quest08) {
        test("one") {
            val sample = """
                13
            """.trimIndent().lines()
            one(sample) shouldBe 21L

            val input = lines(quest, 1)
            one(input) shouldBe 8393577L
        }

        test("two") {
            val sample = """
            """.trimIndent().lines()
            two(sample) shouldBe 0

//            val input = lines(quest, 2)
//            two(input) shouldBe 0
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
