import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

object Quest04 {
    private fun parse(input: List<String>) =
        input.map { l -> Regex("""\d+""").findAll(l).map { it.value.toInt() }.toList() }

    fun one(input: List<String>): Int {
        return parse(input).groupBy { it[0] }.values.map { ql -> ql.sumOf { it[3] } }.sorted().withIndex()
            .sumOf { (i, v) -> (i + 1) * v }
    }

    fun two(input: List<String>): Int {
        return parse(input).groupBy { it[0] }.values.map { ql -> [ql.size, ql.sumOf { it[3] }] }
            .sortedWith(compareByDescending<List<Int>> { it[0] }.thenBy { it[1] }).withIndex()
            .sumOf { (i, v) -> (i + 1) * v[1] }
    }

    fun three(input: List<String>): Int {
        val data = parse(input)
        val points = data.map { it[0] }.associateWith { 0 }.toMutableMap()
        for (quest in data.map { it[1] }.distinct().sorted()) {
            for (part in 1..3) {
                val ids = data.filter { it[1] == quest && it[2] == part }.map { it[0] }.toSet()
                var point = 0
                var prev: Int? = null
                data.filter { it[0] in ids && it[1] == quest && it[2] <= part }
                    .groupBy { it[0] }.entries.map { (k, v) -> [k, v.sumOf { it[3] }] }.sortedBy { it[1] }.withIndex()
                    .forEach { (i, v) ->
                        if (v[1] != prev) point = maxOf(0, 50 * part - i)
                        points[v[0]] = points[v[0]]!! + point
                        prev = v[1]
                    }
            }
        }
        var pv: Int? = null
        var pi = 0
        return points.values.sortedDescending().withIndex()
            .sumOf { (i, v) ->
                pi = if (v != pv) i else pi
                pv = v
                pv * (pi + 1) }
    }
}

val Quest04Test by testSuite {
    val quest = "04"

    with(Quest04) {
        test("one") {
            val sample = """
                id=1 quest=1 part=1 time=187
                id=3 quest=1 part=1 time=413
                id=5 quest=1 part=3 time=744
                id=2 quest=1 part=3 time=745
                id=4 quest=1 part=2 time=558
                id=1 quest=1 part=2 time=299
                id=1 quest=1 part=3 time=362
                id=4 quest=1 part=3 time=1268
                id=4 quest=1 part=1 time=457
                id=5 quest=1 part=1 time=257
                id=2 quest=1 part=1 time=192
                id=3 quest=1 part=3 time=1168
                id=5 quest=1 part=2 time=346
                id=3 quest=1 part=2 time=455
                id=2 quest=1 part=2 time=562
            """.trimIndent().lines()
            one(sample) shouldBe 27598

            val input = lines(quest, 1)
            one(input) shouldBe 193518
        }

        test("two") {
            val sample = """
                id=2 quest=1 part=1 time=100
                id=4 quest=2 part=1 time=150
                id=1 quest=2 part=1 time=140
                id=1 quest=1 part=2 time=180
                id=2 quest=2 part=1 time=130
                id=4 quest=1 part=1 time=110
                id=1 quest=2 part=2 time=210
                id=2 quest=2 part=2 time=240
                id=1 quest=2 part=3 time=330
                id=2 quest=1 part=2 time=170
                id=3 quest=1 part=1 time=90
                id=4 quest=1 part=2 time=175
                id=3 quest=2 part=1 time=120
                id=1 quest=1 part=1 time=120
                id=3 quest=2 part=2 time=220
                id=2 quest=2 part=3 time=350
                id=3 quest=1 part=2 time=160
            """.trimIndent().lines()
            two(sample) shouldBe 6470

            val input = lines(quest, 2)
            two(input) shouldBe 612030142
        }

        test("three") {
            val sample1 = """
                id=2 quest=1 part=1 time=100
                id=4 quest=2 part=1 time=150
                id=1 quest=2 part=1 time=140
                id=1 quest=1 part=2 time=180
                id=2 quest=2 part=1 time=130
                id=4 quest=1 part=1 time=110
                id=1 quest=2 part=2 time=210
                id=2 quest=2 part=2 time=240
                id=1 quest=2 part=3 time=330
                id=2 quest=1 part=2 time=170
                id=3 quest=1 part=1 time=90
                id=4 quest=1 part=2 time=175
                id=3 quest=2 part=1 time=120
                id=1 quest=1 part=1 time=120
                id=3 quest=2 part=2 time=220
                id=2 quest=2 part=3 time=350
                id=3 quest=1 part=2 time=160
            """.trimIndent().lines()
            three(sample1) shouldBe 2998

            val sample2 = """
                id=2 quest=1 part=1 time=90
                id=4 quest=2 part=1 time=150
                id=1 quest=2 part=1 time=130
                id=1 quest=1 part=2 time=180
                id=2 quest=2 part=1 time=130
                id=4 quest=1 part=1 time=110
                id=1 quest=2 part=2 time=210
                id=2 quest=2 part=2 time=220
                id=1 quest=2 part=3 time=330
                id=2 quest=1 part=2 time=170
                id=3 quest=1 part=1 time=90
                id=4 quest=1 part=2 time=170
                id=3 quest=2 part=1 time=120
                id=1 quest=1 part=1 time=120
                id=3 quest=2 part=2 time=220
                id=2 quest=2 part=3 time=350
                id=3 quest=1 part=2 time=170
            """.trimIndent().lines()
            three(sample2) shouldBe 3004


            val input = lines(quest, 3)
            three(input) shouldBe 6839629
        }
    }
}
