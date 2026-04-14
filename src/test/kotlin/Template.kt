import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

object QuestXX {
    private fun parse(input: List<String>) = input

    fun one(input: List<String>): Int {
        return 0
    }

    fun two(input: List<String>): Int {
        return 0
    }

    fun three(input: List<String>): Int {
        return 0
    }
}

val QuestXXTest by testSuite {
    val quest = "XX"

    with(QuestXX) {
        test("one") {
            val sample = """
            """.trimIndent().lines()
            one(sample) shouldBe 0

//            val input = lines(quest, 1)
//            one(input) shouldBe 0
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
