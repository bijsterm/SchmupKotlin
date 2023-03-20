package nl.bijster.kotlin.schmup.types


class HiScoreTable() {


    companion object HiScores {

        private val scoreComparator = compareByDescending<Pair<Int, Double>> { it.first }
        private val ScoreAndLevelComparator = scoreComparator.thenByDescending { it.second }

        // Initialize with 10 zero scores
        private var hiScores = MutableList(10) { Pair(0, 1.0) }

        fun get(key: Int): Pair<Int, Double> {
            return hiScores[key]
        }

        fun get(): List<Pair<Int, Double>> {
            return hiScores
        }

        fun add(key: Int, value: Double) {
            hiScores.add(Pair(key, value))
            hiScores = hiScores.sortedWith(ScoreAndLevelComparator).take(10).toMutableList()
        }

    }
}
