package us.elang.battleline

import com.jhood.battlebot._
import us.elang.battleline.Constants._

class ExpValueCardCalculator extends MoveCalculator {
  override def compute_play(myDirection: Direction,
                            myHand: List[Card],
                            myFlags: Map[Int, List[Card]],
                            opponentFlags: Map[Int, List[Card]],
                            claimedFlags: Map[Int, Direction]): PlayCardResponse = {

    val completeFlags = myFlags.filter(pair => pair._2.size == 3).keySet
    val availableFlags = flags.toSet -- claimedFlags.keys.toSet -- completeFlags

    def bestPlay(flag: Int) = {
      val (card, score) = HandCalculator(myHand.toSet, myFlags.getOrElse(flag, Nil).toSet)
      (PlayCardResponse(flag, card), score)
    }

    availableFlags.map(bestPlay)
                  .maxBy({case (play, score) => score})
                  ._1
  }
}

