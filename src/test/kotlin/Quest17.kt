import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

object Quest17 {
    private fun parse(input: List<String>) = CharArea(input)

    fun one(input: List<String>): Int {
        val area = parse(input)
        val stars = area.tiles('*')
        val connected = mutableSetOf(stars.first())
        val remaining = stars.drop(1).toMutableSet()
        var distance = 0
        while (remaining.isNotEmpty()) {
            val n = remaining.minBy { r -> connected.minOf { c -> manhattanDistance(r, c) } }
            distance += connected.minOf { c -> manhattanDistance(n, c) }
            connected += n
            remaining -= n
        }
        return connected.size + distance
    }

    fun two(input: List<String>): Int {
        return one(input)
    }

    fun three(input: List<String>): Long {
        val area = parse(input)
        val stars = area.tiles('*')
        val groups = mutableListOf(mutableSetOf(stars.first()))
        for (star in stars.drop(1)) {
            val g = groups.filter { g -> g.minOf { manhattanDistance(star, it) } < 6 }
            if (g.isEmpty()) {
                groups += mutableSetOf(star)
            } else {
                g.first().add(star)
                if (g.size > 1) {
                    groups += g.flatten().toMutableSet()
                    groups -= g.toSet()
                }
            }
        }

        val sizes = mutableListOf<Long>()
        for (group in groups) {
            val connected = mutableSetOf(group.first())
            val remaining = group.drop(1).toMutableSet()
            var distance = 0L
            while (remaining.isNotEmpty()) {
                val n = remaining.minBy { r -> connected.minOf { c -> manhattanDistance(r, c) } }
                distance += connected.minOf { c -> manhattanDistance(n, c) }
                connected += n
                remaining -= n
            }
            sizes += connected.size + distance
        }

        return sizes.sorted().takeLast(3).product()
    }
}

val Quest17Test by testSuite {
    val quest = "17"

    with(Quest17) {
        test("one") {
            val sample = """
                *...*
                ..*..
                .....
                .....
                *.*..
            """.trimIndent().lines()
            one(sample) shouldBe 16

            val input = lines(quest, 1)
            one(input) shouldBe 132
        }

        test("two") {
            val input = lines(quest, 2)
            two(input) shouldBe 1220
        }

        test("three") {
            val sample = """
                .......................................
                ..*.......*...*.....*...*......**.**...
                ....*.................*.......*..*..*..
                ..*.........*.......*...*.....*.....*..
                ......................*........*...*...
                ..*.*.....*...*.....*...*........*.....
                .......................................
            """.trimIndent().lines()
            three(sample) shouldBe 15624L

            val input = lines(quest, 3)
            three(input) shouldBe 5056880400L
        }
    }
}
