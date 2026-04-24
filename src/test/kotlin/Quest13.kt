import de.infix.testBalloon.framework.core.testSuite
import io.kotest.matchers.shouldBe
import kotlin.math.abs
import kotlin.math.min

object Quest13 {
    private fun parse(area: CharArea, goal: Point): Map<Node<String>, List<Edge<String>>> {
        val weights = mutableMapOf<Node<String>, MutableList<Edge<String>>>()
        area.tiles { it.isDigit() }.forEach { p ->
            val pi = Node(p.toString(), manhattanDistance(p, goal))
            val pl = weights.getOrPut(pi) { mutableListOf() }
            area.neighbors4(p) { it.isDigit() }.forEach { n ->
                val weight = weight(p, n, area)
                pl.add(Edge(Node(n.toString(), manhattanDistance(n, goal)), weight))
            }
        }
        return weights
    }

    private fun weight(p1: Point, p2: Point, area: CharArea): Int {
        val d = abs(area[p1] - area[p2])
        return min(d, 10 - d) + 1
    }

    private fun Point.node(e: Point) = Node(toString(), manhattanDistance(this, e))

    fun oneTwo(input: List<String>): Int {
        val area = CharArea(input)
        val s = area.first('S')
        area[s] = '0'
        val e = area.first('E')
        area[e] = '0'
        val graph = parse(area, e)
        val start = s.node(e)
        val goal = e.node(e)
        val path = AStarGraph(graph).findShortestPath(start, goal)
        return path!!.zipWithNext().sumOf { (a, b) -> graph[a]!!.first { edge -> edge.to == b }.weight } ?: 0
    }

    fun three(input: List<String>): Int {
        val area = CharArea(input)
        val e = area.first('E')
        area[e] = '0'
        val goal = Node(e.toString(), 0)
        val ss = area.tiles('S').sortedBy { manhattanDistance(it, e) }
        val graph = parse(area, e)
        var shortest = Int.MAX_VALUE
        for (s in ss) {
            val n = area.neighbors4(s) { it.isDigit() }.firstOrNull() ?: continue
            area[s] = '0'
            val weight = weight(s, n, area)
            val start = s.node(e)
            val g = graph + (start to listOf(Edge(n.node(e), weight)))
            val path = AStarGraph(g).findShortestPath(start, goal)
            if (path != null) {
                val cost = path.zipWithNext().sumOf { (a, b) -> g[a]!!.first { edge -> edge.to == b }.weight }
                shortest = min(shortest, cost)
            }
            area[s] = 'S'
        }
        return shortest
    }
}

val Quest13Test by testSuite {
    val quest = "13"

    with(Quest13) {
        test("one") {
            val sample = """
                #######
                #6769##
                S50505E
                #97434#
                #######
            """.trimIndent().lines()
            oneTwo(sample) shouldBe 28

            val sample2 = """
                #######
                S90000E
                #######
            """.trimIndent().lines()
            oneTwo(sample2) shouldBe 8

            val input = lines(quest, 1)
            oneTwo(input) shouldBe 137
        }

        test("two") {
            val input = lines(quest, 2)
            oneTwo(input) shouldBe 666
        }

        test("three") {
            val sample = """
                SSSSSSSSSSS
                S674345621S
                S###6#4#18S
                S53#6#4532S
                S5450E0485S
                S##7154532S
                S2##314#18S
                S971595#34S
                SSSSSSSSSSS
            """.trimIndent().lines()
            three(sample) shouldBe 14

            val input = lines(quest, 3)
            three(input) shouldBe 543
        }
    }
}
