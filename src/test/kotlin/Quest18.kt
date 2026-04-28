import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

object Quest18 {
    private fun parse(input: List<String>) = CharArea(input)

    private fun oneTwo(area: CharArea, start: Set<Point>): Int {
        val palmCount = area.tiles('P').toList().size
        var water = start
        water.forEach { area[it] = '~' }
        var time = 0
        var watered = 0
        while (true) {
            time++
            water = water.flatMap { p -> area.neighbors4(p) { it == '.' || it == 'P' } }.toSet()
            watered += water.count { area[it] == 'P' }
            if (watered == palmCount) return time
            water.forEach { area[it] = '~' }
        }
    }

    fun one(input: List<String>): Int {
        val area = parse(input)
        val start = Point(0, area.yRange.first { area[0, it] == '.' })
        return oneTwo(area, setOf(start))
    }

    fun two(input: List<String>): Int {
        val area = parse(input)
        val left = Point(area.xRange.first, area.yRange.first { area[area.xRange.first, it] == '.' })
        val right = Point(area.xRange.last, area.yRange.first { area[area.xRange.last, it] == '.' })
        return oneTwo(area, setOf(left, right))
    }

    fun three(input: List<String>): Int {
        val area = parse(input)
        val palms = area.tiles('P').toList()
        val palmCount = palms.size
        var opt = Int.MAX_VALUE

        fun flood(start: Point): Int {
            var water = setOf(start)
            area[start] = '~'
            var time = 0
            var timeSum = 0
            var watered = 0
            while (true) {
                time++
                water = water.flatMap { p -> area.neighbors4(p) { it == '.' || it == 'P' } }.toSet()
                val pc = water.count { area[it] == 'P' }
                watered += pc
                timeSum += pc * time
                if (watered == palmCount) return timeSum
                if (timeSum + time >= opt) return Int.MAX_VALUE
                water.forEach { area[it] = '~' }
            }
        }

        area.tiles('.').forEach { p ->
            opt = minOf(opt, flood(p))
            area.tiles('~').forEach { area[it] = '.' }
            palms.forEach { area[it] = 'P' }
        }
        return opt
    }
}

val Quest18Test by testSuite {
    val quest = "18"

    with(Quest18) {
        test("one") {
            val sample = """
                ##########
                ..#......#
                #.P.####P#
                #.#...P#.#
                ##########
            """.trimIndent().lines()
            one(sample) shouldBe 11

            val input = lines(quest, 1)
            one(input) shouldBe 109
        }

        test("two") {
            val sample = """
                #######################
                ...P..P...#P....#.....#
                #.#######.#.#.#.#####.#
                #.....#...#P#.#..P....#
                #.#####.#####.#########
                #...P....P.P.P.....P#.#
                #.#######.#####.#.#.#.#
                #...#.....#P...P#.#....
                #######################
            """.trimIndent().lines()
            two(sample) shouldBe 21

            val input = lines(quest, 2)
            two(input) shouldBe 1323
        }

        test("three") {
            val sample = """
                ##########
                #.#......#
                #.P.####P#
                #.#...P#.#
                ##########
            """.trimIndent().lines()
            three(sample) shouldBe 12

            val input = lines(quest, 3)
            three(input) shouldBe 262546
        }
    }
}
