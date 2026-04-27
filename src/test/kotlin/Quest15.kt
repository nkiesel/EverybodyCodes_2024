import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe
import kotlin.math.min

object Quest15 {
    private fun parse(input: List<String>) = CharArea(input)

    fun one(input: List<String>): Int {
        val area = parse(input)
        val start = area.tiles('.').first { it.y == 0 }
        val herbs = area.tiles('H')
        var shortest = Int.MAX_VALUE
        val seen = mutableSetOf(start)
        val queue = ArrayDeque(listOf(IndexedValue(0, start)))
        while (queue.isNotEmpty()) {
            val a = queue.removeFirst()
            if (a.index >= shortest) continue
            if (a.value in herbs) {
                shortest = min(shortest, a.index)
                continue
            }
            for (n in area.neighbors4(a.value) { it != '#' }) {
                if (seen.add(n)) queue.add(IndexedValue(a.index + 1, n))
            }
        }
        return shortest * 2
    }

    fun two(input: List<String>): Int {
        return 0
    }

    fun three(input: List<String>): Int {
        return 0
    }
}

val Quest15Test by testSuite {
    val quest = "15"

    with(Quest15) {
        test("one") {
            val sample = """
                #####.#####
                #.........#
                #.######.##
                #.........#
                ###.#.#####
                #H.......H#
                ###########
            """.trimIndent().lines()
            one(sample) shouldBe 26

            val input = lines(quest, 1)
            one(input) shouldBe 184
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
