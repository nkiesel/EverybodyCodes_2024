import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe

object Quest07 {
    private fun parse(input: List<String>) = input.map { it.split(":") }.associate { (k, v) -> k to v.replace(",", "") }

    fun one(input: List<String>): String {
        val data = parse(input)
        val values = mutableMapOf<String, Int>()
        for ((k, ops) in data) {
            var v = 10
            var s = 0
            repeat(10) { r ->
                s += when (ops[r % ops.length]) {
                    '+' -> ++v
                    '-' -> --v
                    else -> v
                }
            }
            values[k] = s
        }
        return values.entries.sortedByDescending { it.value }.joinToString("") { it.key }
    }

    fun two(input: List<String>, trackData: List<String>): String {
        val data = parse(input)
        val lx = trackData[0].length - 1
        val ly = trackData.size - 1
        val track = trackData[0].drop(1) +
                (1..<ly).map { trackData[it][lx] }.joinToString("") +
                trackData[ly].reversed() +
                (0..<ly).map { trackData[it][0] }.reversed().joinToString("")
        val essences = buildMap {
            for ((plan, ops) in data) {
                var v = 10
                var sum = 0
                for (ti in 0..<(track.length * 10)) {
                    sum += when (track[ti % track.length]) {
                        '+' -> ++v
                        '-' -> --v
                        else -> when (ops[ti % ops.length]) {
                            '+' -> ++v
                            '-' -> --v
                            else -> v
                        }
                    }
                }
                put(plan, sum)
            }
        }
        return essences.entries.sortedByDescending { it.value }.joinToString("") { it.key }
    }

    fun three(input: List<String>, trackData: List<String>): Int {
        val rivalOps = parse(input).values.first().toList()
        val area = CharArea(trackData)
        var pos = Point(1, 0)
        val s = Point(0, 0)
        var prev = s
        val track = buildList {
            add(area[pos])
            do {
                val n = area.neighbors4(pos) { it != ' ' }.first { it != prev }
                prev = pos
                pos = n
                add(area[pos])
            } while (pos != s)
        }
        val ts = track.size
        val os = rivalOps.size

        fun essence(ops: List<Char>): Long {
            var v = 10L
            return (0..<(ts * 2024)).sumOf { ti ->
                when (track[ti % ts]) {
                    '+' -> ++v
                    '-' -> --v
                    else -> when (ops[ti % os]) {
                        '+' -> ++v
                        '-' -> --v
                        else -> v
                    }
                }
            }
        }

        val rival = essence(rivalOps)
        val seen = mutableSetOf<List<Char>>()
        return "+++++---===".toList().permutations().count { seen.add(it) && essence(it) > rival }
    }
}

val Quest07Test by testSuite {
    val quest = "07"

    with(Quest07) {
        test("one") {
            val sample = """
                A:+,-,=,=
                B:+,=,-,+
                C:=,-,+,+
                D:=,=,=,+
            """.trimIndent().lines()
            one(sample) shouldBe "BDCA"

            val input = lines(quest, 1)
            one(input) shouldBe "AJCDIBFGH"
        }

        test("two") {
            val sampleData = """
                A:+,-,=,=
                B:+,=,-,+
                C:=,-,+,+
                D:=,=,=,+
            """.trimIndent().lines()
            val sampleTrack = """
                S+===
                -   +
                =+=-+
            """.trimIndent().lines()
            two(sampleData, sampleTrack) shouldBe "DCBA"

            val input = lines(quest, 2)
            val track = """
                S-=++=-==++=++=-=+=-=+=+=--=-=++=-==++=-+=-=+=-=+=+=++=-+==++=++=-=-=--
                -                                                                     -
                =                                                                     =
                +                                                                     +
                =                                                                     +
                +                                                                     =
                =                                                                     =
                -                                                                     -
                --==++++==+=+++-=+=-=+=-+-=+-=+-=+=-=+=--=+++=++=+++==++==--=+=++==+++-
            """.trimIndent().lines()
            two(input, track) shouldBe "IGDEJFBHK"
        }

        test("three") {
            val track = """
                S+= +=-== +=++=     =+=+=--=    =-= ++=     +=-  =+=++=-+==+ =++=-=-=--
                - + +   + =   =     =      =   == = - -     - =  =         =-=        -
                = + + +-- =-= ==-==-= --++ +  == == = +     - =  =    ==++=    =++=-=++
                + + + =     +         =  + + == == ++ =     = =  ==   =   = =++=       
                = = + + +== +==     =++ == =+=  =  +  +==-=++ =   =++ --= + =          
                + ==- = + =   = =+= =   =       ++--          +     =   = = =--= ==++==
                =     ==- ==+-- = = = ++= +=--      ==+ ==--= +--+=-= ==- ==   =+=    =
                -               = = = =   +  +  ==+ = = +   =        ++    =          -
                -               = + + =   +  -  = + = = +   =        +     =          -
                --==++++==+=+++-= =-= =-+-=  =+-= =-= =--   +=++=+++==     -=+=++==+++-
            """.trimIndent().lines()
            val input = lines(quest, 3)
            three(input, track) shouldBe 4275
        }
    }
}
