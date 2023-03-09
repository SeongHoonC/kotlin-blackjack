package domain.result

import domain.card.CardNumber
import domain.card.Cards
import domain.constant.BlackJackConstants.BIG_ACE
import domain.constant.BlackJackConstants.BLACK_JACK_NUMBER
import domain.constant.BlackJackConstants.NOTHING
import domain.constant.BlackJackConstants.SMALL_ACE

object CardsScore {

    fun getTotalWithOneBigAce(cards: Cards): Int {
        val sumExceptAce: Int = sumExceptAce(cards)
        return sumExceptAce + sumAce(sumExceptAce, cards.countAce())
    }

    fun getTotalWithSmallAce(cards: Cards): Int = sumExceptAce(cards) + cards.countAce()

    private fun sumExceptAce(cards: Cards) = cards.value
        .filter { it.number != CardNumber.ACE }
        .sumOf { it.number.value }

    private fun sumAce(sumExceptAce: Int, aceCount: Int): Int {
        val maxAceSum = BIG_ACE + (aceCount - 1) * SMALL_ACE
        return when {
            aceCount == NOTHING -> NOTHING
            BLACK_JACK_NUMBER - sumExceptAce >= maxAceSum -> maxAceSum
            else -> aceCount * SMALL_ACE
        }
    }
}
