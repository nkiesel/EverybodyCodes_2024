import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.ints.shouldBeInRange
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

object Quest10 {
    private fun fillSample(input: List<String>): Pair<String, CharArea> {
        val area = CharArea(input)
        val rowChars = area.yRange.map { y -> area.xRange.map { x -> area[x, y] }.filter(Char::isLetter).toSet() }
        val colChars = area.xRange.map { x -> area.yRange.map { y -> area[x, y] }.filter(Char::isLetter).toSet() }
        return buildString {
            area.tiles('.').forEach { dot ->
                val c = rowChars[dot.y].intersect(colChars[dot.x]).firstOrNull()
                if (c != null) {
                    area[dot] = c
                    append(c)
                } else {
                    append('.')
                }
            }
        } to area
    }

    fun one(input: List<String>): String = fillSample(input).first

    fun two(input: List<String>): Int {
        val nx = input[0].length
        val ny = input.size
        var total = 0
        val ao = 'A'.code - 1
        for (y in 0..<ny step 9) {
            for (x in 0..<nx step 9) {
                val sample = input.subList(y, y + 8).map { it.substring(x, x + 8) }
                total += one(sample).mapIndexed { index, ch -> (index + 1) * (ch.code - ao) }.sum()
            }
        }
        return total
    }

    fun three(input: List<String>): Int {
        val data = input.toMutableList()
        val nx = data[0].length
        val ny = data.size
        val ao = 'A'.code - 1
        var total = 0
        for (y in 0..<ny step 6) {
            if (ny < y + 8) continue
            for (x in 0..<nx step 6) {
                if (nx < x + 8) continue
                val sample = data.subList(y, y + 8).map { it.substring(x, x + 8) }
                val (runic, area) = fillSample(sample)
                val r = runic.toCharArray()
                val updated = mutableMapOf<Point, Char>()
                if ('.' in r) {
                    area.tiles('.').forEach { dot ->
                        val row = area.row(dot.y)
                        val column = area.column(dot.x)
                        val chars = (row.toList() + column.toList()).groupingBy { it }.eachCount()
                        if ('?' in chars.keys) {
                            val c = chars.entries.find { it.key.isLetter() && it.value == 1 }?.key
                            if (c != null) {
                                area[dot] = c
                                r[(dot.y - 2) * 4 + dot.x - 2] = c
                                val qx = row.indexOf('?')
                                if (qx != -1) updated[Point(x + qx, y + dot.y)] = c
                                val qy = column.indexOf('?')
                                if (qy != -1) updated[Point(x + dot.x, y + qy)] = c
                            }
                        }
                    }
                }
                if ('.' !in r) {
                    updated.forEach { (p, c) -> data[p.y] = data[p.y].replaceRange(p.x, p.x + 1, c.toString()) }
                    val sum = r.mapIndexed { index, ch -> (index + 1) * (ch.code - ao) }.sum()
                    total += sum
//                    println("${x / 6 + 1},${y / 6 + 1}: $sum")
                } else {
//                    println("${x / 6 + 1},${y / 6 + 1}: nothing")
                }
            }
        }
        return total
    }
}

val Quest10Test by testSuite {
    val quest = "10"

    with(Quest10) {
        test("one") {
            val sample = """
                **PCBS**
                **RLNW**
                BV....PT
                CR....HZ
                FL....JW
                SG....MN
                **FTZV**
                **GMJH**
            """.trimIndent().lines()
            one(sample) shouldBe "PTBVRCZHFLJWGMNS"

            val input = lines(quest, 1)
            one(input) shouldBe "RQLXBVHTWCZMGKND"
        }

        test("two") {
            val sample = """
                **PCBS**
                **RLNW**
                BV....PT
                CR....HZ
                FL....JW
                SG....MN
                **FTZV**
                **GMJH**
            """.trimIndent().lines()
            two(sample) shouldBe 1851

            val input = lines(quest, 2)
            two(input) shouldBe 197747
        }

        test("three") {
            val sample = """
                **XFZB**DCST**
                **LWQK**GQJH**
                ?G....WL....DQ
                BS....H?....CN
                P?....KJ....TV
                NM....Z?....SG
                **NSHM**VKWZ**
                **PJGV**XFNL**
                WQ....?L....YS
                FX....DJ....HV
                ?Y....WM....?J
                TJ....YK....LP
                **XRTK**BMSP**
                **DWZN**GCJV**
            """.trimIndent().lines()
            three(sample) shouldBe 3889

            val input = lines(quest, 3)
            val three = three(input)
            three shouldNotBe 197687
            three shouldNotBe 208981
            three shouldBeInRange 200000..<300000
            three shouldBe 0
        }
    }
}
