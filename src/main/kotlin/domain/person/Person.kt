package domain.person

import domain.card.Card
import domain.card.CardNumber
import domain.constant.BlackJackConstants.BIG_ACE
import domain.constant.BlackJackConstants.BLACK_JACK
import domain.constant.BlackJackConstants.NOTHING
import domain.constant.BlackJackConstants.SMALL_ACE

abstract class Person(open val name: String) {
    private val _cards = mutableListOf<Card>()
    val cards: List<Card> get() = _cards.toList()
    fun receiveCard(vararg card: Card) {
        card.forEach { _cards.add(it) }
        checkState()
    }

    protected abstract fun checkState(): GameState

    fun isState(state: GameState) = checkState() == state

    fun getTotalCardNumber(): Int {
        val sumExceptAce: Int = calculateSumExceptAce()
        val aceCount: Int = countAce()
        return sumExceptAce + calculateSumAce(BLACK_JACK - sumExceptAce, aceCount)
    }

    protected fun countAce() = cards.count { it.number == CardNumber.ACE }
    protected fun calculateSumExceptAce() = cards
        .filter { it.number != CardNumber.ACE }
        .sumOf { it.number.value }

    private fun calculateSumAce(availableMax: Int, aceCount: Int) = when {
        aceCount == NOTHING -> NOTHING
        availableMax >= BIG_ACE + aceCount - 1 -> BIG_ACE + (aceCount - 1) * SMALL_ACE
        else -> aceCount * SMALL_ACE
    }
}
