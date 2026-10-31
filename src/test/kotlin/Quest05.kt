import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

object Quest05 {
    private fun parse(input: List<String>) = CharArea(input)

    fun one(input: List<String>): Int = solution(input, Part.ONE)
    fun two(input: List<String>): Int = solution(input, Part.TWO)
    fun three(input: List<String>): Int = solution(input, Part.THREE)

    fun solution(input: List<String>, part: Part): Int {
        val area = parse(input)
        area[area.xRange.last / 2, area.yRange.last / 2] = '#'
        do {
            var landed = false
            area.tiles('#').flatMap { l -> area.neighbors8(l) { it == '0' || it == '1' } }.toSet().forEach { p ->
                if (area.neighbors8(p) { it == '#' || it == '1' }.size >= 5) {
                    area[p] = '#'
                    landed = true
                }
            }
        } while (landed)

        if (part == Part.ONE) return area.tiles('#').count()

        area.tiles('0', '1').forEach { p -> area[p] = '.' }
        area.tiles('#').filter { l -> l in area.edges() || area.neighbors8(l, '.').isNotEmpty() }.forEach { area[it] = '=' }
        do {
            var sanded = false
            area.tiles('#').filter { s -> area.neighbors8(s, '=').count() >= 5 }.forEach {
                area[it] = '='
                sanded = true
            }
        } while (sanded)

        if (part == Part.TWO) return area.tiles('=').count()

        area.tiles('#').forEach { l ->
            var n = 0
            for (x in l.x - 2..l.x + 2) {
                for (y in l.y - 2..l.y + 2) {
                    if ((x == l.x - 2 || x == l.x + 2) && (y == l.y - 2 || y == l.y + 2)) continue
                    val c = area[x, y]
                    if (c == '#' || c == '@') n++
                }
            }
            if (n == 21) area[l] = '@'
        }
        repeat(2) {
            buildList {
                area.tiles('#').forEach { l ->
                    if (area.neighbors8(l, '@').count() >= 4 && area.neighbors8(l, '=').isEmpty()) add(l)
                }
            }.forEach { area[it] = '@' }
        }
        return area.tiles('@').count()
    }
}

val Quest05Test by testSuite {
    val quest = "05"

    with(Quest05) {
        test("one") {
            val sample = """
                010100100100001101000
                111101001000001110000
                101111011000101010011
                001000101010000100000
                000110100110011001100
                000001110111111000000
                101100010101011110000
                001101100000001010011
                001001001111010001001
                100011011110011010101
                001100101110010010000
            """.trimIndent().lines()
            one(sample) shouldBe 137

            val input = lines(quest, 1)
            one(input) shouldBe 1172
        }

        test("two") {
            val sample = """
                010100100100001101000
                111101001000001110000
                101111011000101010011
                001000101010000100000
                000110100110011001100
                000001110111111000000
                101100010101011110000
                001101100000001010011
                001001001111010001001
                100011011110011010101
                001100101110010010000
            """.trimIndent().lines()
            two(sample) shouldBe 69

            val input = lines(quest, 2)
            two(input) shouldBe 1304
        }

        test("three") {
            val sample = """
                010100100100001101000
                111101001000001110000
                101111011000101010011
                001000101010000100000
                000110100110011001100
                000001110111111000000
                101100010101011110000
                001101100000001010011
                001001001111010001001
                100011011110011010101
                001100101110010010000
            """.trimIndent().lines()
            three(sample) shouldBe 14

            val input = lines(quest, 3)
            three(input) shouldBe 9087
        }
    }
}
