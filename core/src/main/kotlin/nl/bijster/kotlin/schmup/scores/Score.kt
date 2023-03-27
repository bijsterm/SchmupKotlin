package nl.bijster.kotlin.schmup.scores

class Score {

    companion object Score {
        var score: Int = 0

        operator fun plus(scoreToAdd: Int): Int {
            return score + scoreToAdd
        }

        operator fun plusAssign(scoreToAdd: Int) {
            score += scoreToAdd
        }

    }
}
