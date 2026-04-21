import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

object Quest12 {
    fun one(input: List<String>) = oneTwo(input)

    fun two(input: List<String>) = oneTwo(input)

    private fun oneTwo(input: List<String>): Int {
        val area = CharArea(input)
        val base = area.first('=').y
        val targets = area.tiles('T').map { Point(it.x - 1, base - it.y - 1) }
        val hardRocks = area.tiles('H').map { Point(it.x - 1, base - it.y - 1) }
        return (targets + hardRocks).sortedBy { it.y }.sumOf { target ->
            val distance = target.x + target.y
            distance / 3 * (distance % 3 + 1) * (if (target in hardRocks) 2 else 1)
        }
    }

    fun three(input: List<String>): Int {
        return 0
    }
}

val Quest12Test by testSuite {
    val quest = "12"

    with(Quest12) {
        test("one") {
            val sample = """
                .............
                .C...........
                .B......T....
                .A......T.T..
                =============
            """.trimIndent().lines()
            one(sample) shouldBe 13

            val input = lines(quest, 1)
            one(input) shouldBe 204
        }

        test("two") {
            val sample = """
                .............
                .C...........
                .B......H....
                .A......T.H..
                =============
            """.trimIndent().lines()
            two(sample) shouldBe 22

            val input = lines(quest, 2)
            two(input) shouldBe 20577
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
