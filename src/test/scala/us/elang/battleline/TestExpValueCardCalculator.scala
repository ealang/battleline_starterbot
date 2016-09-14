package us.elang.battleline

import com.jhood.battlebot._
import org.scalatest.{Matchers, WordSpec}
import us.elang.battleline.Constants._

class TestExpValueCardCalculator extends WordSpec with Matchers {
  "ExpValueCardCalculator" should {
    "Always give best card to most valuable flag" in {
      val response = new ExpValueCardCalculator().compute_play(
        myDirection=North,
        myHand=List(Card("s1", 4), Card("s1", 3), Card("s4", 9)),
        myFlags=Map(
          1 -> List(Card("s1", 1)),
          2 -> List(Card("s2", 9), Card("s3", 8)),
          3 -> List(Card("s1", 2))),
        opponentFlags=Map[Int, List[Card]](),
        claimedFlags=Map[Int, Direction]())

      response should be (PlayCardResponse(3, Card("s1", 4)))
    }

    "Not try to play to a flag that has been claimed" in {
      val response = new ExpValueCardCalculator().compute_play(
        myDirection=North,
        myHand=List(Card("s1", 4), Card("s1", 3), Card("s4", 9)),
        myFlags=Map(
          1 -> List(Card("s1", 1)),
          2 -> List(Card("s2", 9), Card("s3", 8)),
          3 -> List(Card("s1", 2))),
        opponentFlags=Map[Int, List[Card]](),
        claimedFlags=Map(3 -> South))

      response should be (PlayCardResponse(1, Card("s1", 4)))
    }

    "Not try to play to a flag that is full" in {
      val allFlags: Map[Int, List[Card]] = flags.map(flag => {
        flag -> (1 to 3).map(Card("r1", _)).toList
      }).toMap

      val response = new ExpValueCardCalculator().compute_play(
        myDirection=North,
        myHand=List(Card("s1", 4)),
        myFlags=allFlags + (9 -> List(Card("s2", 5), Card("s1", 5))),
        opponentFlags=Map[Int, List[Card]](),
        claimedFlags=Map(1 -> North, 3 -> North))

      response should be (PlayCardResponse(9, Card("s1", 4)))
    }
  }
}
