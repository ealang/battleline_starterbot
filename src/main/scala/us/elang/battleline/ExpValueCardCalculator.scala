package us.elang.battleline

import com.jhood.battlebot._

object Constants {
  val leftFlag = 1
  val rightFlag = 9
  val flags = leftFlag to rightFlag
  def validFlagPos(flag: Int) = flag >= leftFlag && flag <= rightFlag
}

object FlagValueCalculator {
  import Constants._

  def apply(direction: Direction, claimedFlags: Map[Int, Direction]): Map[Int, Int] = {
    def isMyFlag(flag: Int) = validFlagPos(flag) &&
                              claimedFlags.get(flag).contains(direction)
    flags.map(flag => {
      flag ->
      (flag - 1 to flag + 1).count(isMyFlag)
    }).toMap
  }

  def apply(claimedFlags: Map[Int, Direction]): Map[Int, Int] = {
    val cellValue = Map(1 -> 0, 2 -> 1, 3 -> 1, 4 -> 1,
                        5 -> 1, 6 -> 1, 7 -> 1, 8 -> 1, 9 -> 0)
    val nf = FlagValueCalculator(North, claimedFlags)
    val sf = FlagValueCalculator(South, claimedFlags)

    flags.map(flag => {
      flag -> {
        cellValue(flag) + Math.max(nf(flag), sf(flag))
      }
    }).filter(pair => !claimedFlags.contains(pair._1))
      .toMap
  }
}

// Determine best possible hand that can be completed
object BestCompletedFlagCalculator {

  private def getMaxHand(myHand: Set[Card], fixedCards: Set[Card]): Int = {
    def recurse(myHand: Set[Card], fixedCards: Set[Card]): Int = {
      if (fixedCards.size == 3) {
        FormationCalc.score(fixedCards.toList)
      } else {
        myHand.map(card => {
          recurse(myHand - card, fixedCards + card)
        }).max
      }
    }
    recurse(myHand, fixedCards)
  }

  // return card and relative score
  def apply(myHand: Set[Card], fixedCards: Set[Card]): (Card, Int) = {
    myHand.map(card => (card, getMaxHand(myHand - card, fixedCards + card)))
          .maxBy(_._2)
  }
}

class ExpValueCardCalculator extends MoveCalculator {
  import Constants._

  override def compute_play(myDirection: Direction,
                            myHand: List[Card],
                            myFlags: Map[Int, List[Card]],
                            opponentFlags: Map[Int, List[Card]],
                            claimedFlags: Map[Int, Direction]): PlayCardResponse = {

    val unclaimedFlags = flags.toSet -- claimedFlags.keys.toSet -- myFlags.filter(pair => pair._2.size == 3).keySet

    val (response, _) = unclaimedFlags.map(flag => {
      val (card, score) = BestCompletedFlagCalculator(myHand.toSet,
                                                      myFlags.getOrElse(flag, Nil).toSet)
      (PlayCardResponse(flag, card), score)
    }).maxBy(_._2)

    response
  }
}

