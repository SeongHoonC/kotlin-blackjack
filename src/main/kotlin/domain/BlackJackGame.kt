package domain

import domain.card.Deck
import domain.constant.GameState
import domain.money.Money
import domain.person.Participants
import domain.person.Player
import domain.result.GameResult

class BlackJackGame(private val deck: Deck, private val participants: Participants) {

    fun handOutCardsToPlayers(
        getPlayerWantContinue: (String) -> Boolean,
        printPlayerCards: (Player) -> Unit,
    ) {
        participants.players.forEach { player -> handOutCardsToPlayer(player, getPlayerWantContinue, printPlayerCards) }
    }

    private fun handOutCardsToPlayer(
        player: Player,
        getPlayerWantContinue: (String) -> Boolean,
        printPlayerCards: (Player) -> Unit,
    ) {
        var decision = true
        while (player.isState(GameState.HIT) && decision) {
            decision = applyPlayerDecision(player, getPlayerWantContinue)
            printPlayerCards(player)
        }
    }

    private fun applyPlayerDecision(player: Player, getPlayerWantContinue: (String) -> Boolean): Boolean {
        val decision = getPlayerWantContinue(player.name)
        if (!decision) {
            return false
        }
        player.receiveCard(deck.getCards(1))
        return true
    }

    fun handOutCardsToDealer(printDealerGetCardOrNot: (Boolean) -> Unit) {
        if (participants.dealer.isState(GameState.HIT)) {
            participants.dealer.receiveCard(deck.getCards(1))
            printDealerGetCardOrNot(true)
            return
        }
        printDealerGetCardOrNot(false)
    }

    fun judgeResult(
        playerBettingMoneys: Map<String, Money>,
        printCardsResult: (Participants) -> Unit,
        printFinalResult: (Double, Map<String, Double>) -> Unit,
    ) {
        printCardsResult(participants)
        val gameResult = GameResult(participants)
        val playersResult = gameResult.getPlayerResult(playerBettingMoneys)
        val dealerResult = gameResult.getDealerResult(playersResult)
        printFinalResult(dealerResult, playersResult)
    }
}
