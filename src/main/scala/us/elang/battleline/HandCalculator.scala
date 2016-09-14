package us.elang.battleline

import com.jhood.battlebot.{FormationCalc, Card}

object HandCalculator {
  private def maxHand(myHand: Set[Card], fixedCards: Set[Card]): Int = {
    myHand.subsets(3 - fixedCards.size)
          .map(cards => FormationCalc.score((cards ++ fixedCards).toList))
          .max
  }

  /**
    * Calculate best possible hand that can be formed
    *
    * @param myHand cards available
    * @param fixedCards cards in flag
    * @return (First card to play, Best possible score)
    */
  def apply(myHand: Set[Card], fixedCards: Set[Card]): (Card, Int) = {
    myHand.map(card => (card, maxHand(myHand - card, fixedCards + card)))
          .maxBy({case (card, score) => score})
  }
}
