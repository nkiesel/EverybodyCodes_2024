import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

object Quest19 {
    private fun parse(input: List<String>): Pair<String, CharArea> {
        return input[0] to CharArea(input.drop(2))
    }

    private val o8 = listOf(-1 to -1, 0 to -1, 1 to -1, 1 to 0, 1 to 1, 0 to 1, -1 to 1, -1 to 0)

    private fun CharArea.rotate(center: Point, direction: Char) {
        val n8 = o8.map { (dx, dy) -> Point(center.x + dx, center.y + dy) }.filter { valid(it) }
        val n8c = n8.map { get(it) }
        if (direction == 'L') {
            for (i in 0..7) {
                set(n8[i], n8c[(i + 1) % 8])
            }
        } else {
            for (i in 1..8) {
                set(n8[i % 8], n8c[i - 1])
            }
        }
    }

    fun one(input: List<String>): String {
        val (direction, area) = parse(input)
        var step = 0
        var center = Point(1, 1)
        while (true) {
            area.rotate(center, direction[step % direction.length])
            val l = area.first('>')
            val r = area.first('<')
            if (l.y == r.y && l.x < r.x) {
                return ((l.x + 1)..<(r.x)).map { area[it, l.y] }.joinToString("")
            }
            center = Point(center.x + 1, center.y)
            if (center.x == area.xRange.last) {
                center = Point(1, center.y + 1)
                if (center.y == area.yRange.last) center = Point(1, 1)
            }
            step++
        }
    }

    fun two(input: List<String>): String {
        val (direction, area) = parse(input)
        repeat(100) {
            var step = 0
            var center = Point(1, 1)
            while (true) {
                area.rotate(center, direction[step % direction.length])
                center = Point(center.x + 1, center.y)
                if (center.x == area.xRange.last) {
                    center = Point(1, center.y + 1)
                    if (center.y == area.yRange.last) break
                }
                step++
            }
        }
        val l = area.first('>')
        val r = area.first('<')
        return ((l.x + 1)..<(r.x)).map { area[it, l.y] }.joinToString("")
    }

    fun three(input: List<String>): String {
        val (direction, area) = parse(input)
        val lx = area.xRange.last
        val ly = area.yRange.last
        data class Solution(val step: Int, val l: Point, val text: String)
        val solutions = mutableListOf<Solution>()
        val limit = 1048576000
        var round = 0
        val invalid = "17659872183.2644"
        while (round < limit) {
            var step = 0
            var center = Point(1, 1)
            while (true) {
                area.rotate(center, direction[step % direction.length])
                var nx = center.x + 1
                var ny = center.y
                if (nx == lx) {
                    ny = center.y + 1
                    if (ny == ly) break
                    nx = 1
                }
                center = Point(nx, ny)
                step++
            }
            round++
            val l = area.first('>')
            val r = area.first('<')
            if (l.y == r.y && l.x < r.x) {
                val solution = Solution(round, l, ((l.x + 1)..<(r.x)).map { area[it, l.y] }.joinToString(""))
//                if (solution.text.length == invalid.length && solution.text[0] == '1') println(solution)
                val prev = solutions.lastOrNull { it.l == solution.l && it.text == solution.text }
                if (prev != null) {
                    val d = round - prev.step
//                    println("LOOOP: ${prev.step} -> $round after $d: ${solution.text}")
                    round += (limit - round) / d * d
                }
                solutions += solution
            }
        }
        return solutions.last().text
    }
}

val Quest19Test by testSuite {
    val quest = "19"

    with(Quest19) {
        test("one") {
            val sample = """
                LR

                >-IN-
                -----
                W---<
            """.trimIndent().lines()
            one(sample) shouldBe "WIN"

            val input = lines(quest, 1)
            one(input) shouldBe "9369351295298988"
        }

        test("two") {
            val sample = """
                RRLL

                A.VI..>...T
                .CC...<...O
                .....EIB.R.
                .DHB...YF..
                .....F..G..
                D.H........
            """.trimIndent().lines()
            two(sample) shouldBe "VICTORY"

            val input = lines(quest, 2)
            two(input) shouldBe "5352852581359258"
        }

        test("three") {
            val input = lines(quest, 3)
            three(input) shouldBe ""
        }
    }
}
