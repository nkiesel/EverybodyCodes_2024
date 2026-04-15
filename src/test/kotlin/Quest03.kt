import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

object Quest03 {
    private fun parse(input: List<String>) = CharArea(input)

    fun one(input: List<String>) = three(input, 4)

    fun two(input: List<String>) = three(input, 4)

    fun three(input: List<String>, nc: Int = 8): Int {
        var area = parse(input)
        var removed = 0
        var level = '1'
        area.tiles('#').forEach { p ->
            area[p] = level
            removed++
        }
        do {
            val neighbors: (Point, Char) -> List<Point> = if (nc == 4) area::neighbors4 else area::neighbors8
            val prev = removed
            val next = area.clone()
            val nextLevel = level + 1
            area.tiles(level).filter { p -> neighbors(p, level).count() == nc }.forEach { p ->
                next[p] = nextLevel
                removed++
            }
            level = nextLevel
            area = next
        } while (removed > prev)
        return removed
    }
}

val Quest03Test by testSuite {
    val quest = "03"

    with(Quest03) {
        test("one") {
            val sample = """
                ..........
                ..###.##..
                ...####...
                ..######..
                ..######..
                ...####...
                ..........
            """.trimIndent().lines()
            one(sample) shouldBe 35

            val input = lines(quest, 1)
            one(input) shouldBe 118
        }

        test("two") {
            val input = lines(quest, 2)
            two(input) shouldBe 2691
        }

        test("three") {
            val sample = """
                ..........
                ..###.##..
                ...####...
                ..######..
                ..######..
                ...####...
                ..........
            """.trimIndent().lines()
            three(sample) shouldBe 29

            val input = lines(quest, 3)
            three(input) shouldBe 9661
        }
    }
}
