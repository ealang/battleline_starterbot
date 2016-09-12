package bot

import scala.util.Random
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

object ExpValueCardCalculator {
  def apply(): ExpValueCardCalculator = {
    new ExpValueCardCalculator
  }
}

class ExpValueCardCalculator extends MoveCalculator {
  override def compute_play(myDirection: Direction,
                            myHand: List[Card],
                            myFlags: Map[Int, List[Card]],
                            opponentFlags: Map[Int, List[Card]],
                            claimedFlags: Map[Int, Direction]): PlayCardResponse = {

    def cardsInFlag(flag: Int) = myFlags.getOrElse(flag, Nil).size
    def flagWithCard(flag: Int, card: Card) = card :: myFlags.getOrElse(flag, Nil)

    val flagValues = FlagValueCalculator(claimedFlags)
    val bestFlag = flagValues.toList
                             .view
                             .filter(pair => cardsInFlag(pair._1) < 3)
                             .sortBy(pair => pair._2)
                             .last._1

    val bestCard = myHand.sortBy(card => {
      val newHand = flagWithCard(bestFlag, card)
      FormationCalc.score(newHand)
    }).last
    PlayCardResponse(bestFlag, bestCard)
  }
}

