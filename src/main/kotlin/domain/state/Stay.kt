package domain.state

import domain.card.Hand
import domain.money.Money
import domain.money.Profit

abstract class Stay(hand: Hand) : Finished(hand) {
    override fun profit(other: State, money: Money): Profit {
        val otherScore = other.getScore()
        val myScore = this.getScore()
        return when {
            other is BlackJack -> getLoseProfit(money)
            other is Bust -> Profit.of(money)
            otherScore.isBiggerThan(myScore) -> Profit.of(money, LOSE_PROFIT_RATE)
            otherScore.isSame(myScore) -> Profit.of(money, DRAW_PROFIT_RATE)
            else -> Profit.of(money)
        }
    }

    abstract fun getLoseProfit(money: Money): Profit

    companion object {
        private const val LOSE_PROFIT_RATE = -1.0
        private const val DRAW_PROFIT_RATE = 0.0
    }
}
