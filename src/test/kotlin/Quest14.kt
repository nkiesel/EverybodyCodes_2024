import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe
import jdk.internal.org.jline.utils.Colors.s
import kotlin.math.abs
import kotlin.math.max

object Quest14 {
    private fun parse(input: List<String>) = input.map { it.split(",") }

    fun one(input: List<String>): Int {
        val steps = parse(input)[0]
        var height = 0
        var max = 0
        for (step in steps) {
            when (step[0]) {
                'U' -> height += step.substring(1).toInt()
                'D' -> height -= step.substring(1).toInt()
                else -> continue
            }
            max = max(max, height)
        }
        return max
    }

    private data class Segment(val x: Int, val y: Int, val z: Int) {
        fun move(direction: Char): Segment {
            return when (direction) {
                'U' -> copy(y = y + 1)
                'D' -> copy(y = y - 1)
                'L' -> copy(x = x - 1)
                'R' -> copy(x = x + 1)
                'F' -> copy(z = z - 1)
                'B' -> copy(z = z + 1)
                else -> error("Unexpected direction $direction")
            }
        }

        fun manhattanDistance(o: Segment): Int = manhattanDistance(x, y, z, o.x, o.y, o.z)

        fun neighbors(others: List<Segment>): List<Segment> {
            return others.filter { manhattanDistance(it) == 1 }
        }
    }

    fun two(input: List<String>): Int {
        val branches = parse(input)
        val segments = mutableSetOf<Segment>()
        for (branch in branches) {
            var seg = Segment(0, 0, 0)
            for (step in branch) {
                val d = step[0]
                val c = step.substring(1).toInt()
                repeat(c) {
                    seg = seg.move(d)
                    segments += seg
                }
            }
        }
        return segments.size
    }

    fun three(input: List<String>): Int {
        val branches = parse(input)
        val segments = mutableSetOf<Segment>()
        val leaves = mutableSetOf<Segment>()
        for (branch in branches) {
            var seg = Segment(0, 0, 0)
            for (step in branch) {
                val d = step[0]
                val c = step.substring(1).toInt()
                repeat(c) {
                    seg = seg.move(d)
                    segments += seg
                }
            }
            leaves += seg
        }

        var murkiness = Int.MAX_VALUE
        for (mb in segments.filter { it.x == 0 && it.z == 0 }) {
            val nodes = segments.map { Node(it, it.manhattanDistance(mb)) }
            val edges = nodes.associateWith { node ->
                node.name.neighbors(segments.toList()).map { n -> Edge(nodes.first { it.name == n }, 1) }
            }
            val graph = AStarGraph(edges)
            val paths = leaves.map { leave -> graph.findShortestPath(nodes.first { it.name == leave }, nodes.first() { it.name == mb }) }
            if (paths.none { it == null }) {
                val s = paths.sumOf { it!!.size } - paths.size
                murkiness = minOf(murkiness, s)
            }
        }
        return murkiness
    }
}

val Quest14Test by testSuite {
    val quest = "14"

    with(Quest14) {
        test("one") {
            val sample = """
                U5,R3,D2,L5,U4,R5,D2
            """.trimIndent().lines()
            one(sample) shouldBe 7

            val input = lines(quest, 1)
            one(input) shouldBe 147
        }

        test("two") {
            val sample = """
                U5,R3,D2,L5,U4,R5,D2
                U6,L1,D2,R3,U2,L1
            """.trimIndent().lines()
            two(sample) shouldBe 32

            val input = lines(quest, 2)
            two(input) shouldBe 4818
        }

        test("three") {
            val sample1 = """
                U5,R3,D2,L5,U4,R5,D2
                U6,L1,D2,R3,U2,L1
            """.trimIndent().lines()
            three(sample1) shouldBe 5

            val sample2 = """
                U20,L1,B1,L2,B1,R2,L1,F1,U1
                U10,F1,B1,R1,L1,B1,L1,F1,R2,U1
                U30,L2,F1,R1,B1,R1,F2,U1,F1
                U25,R1,L2,B1,U1,R2,F1,L2
                U16,L1,B1,L1,B3,L1,B1,F1
            """.trimIndent().lines()
            three(sample2) shouldBe 46

            val input = lines(quest, 3)
            three(input) shouldBe 1436
        }
    }
}
