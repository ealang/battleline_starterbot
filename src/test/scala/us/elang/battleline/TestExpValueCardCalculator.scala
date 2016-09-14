package us.elang.battleline

import com.jhood.battlebot._
import org.scalatest.{Matchers, WordSpec}

class TestExpValueCardCalculator extends WordSpec with Matchers {
  import Constants._

  "A FlagValueCalculator" should {
    "give low value to edge flags" in {
      val v = FlagValueCalculator(Map())
      v(1) should be (0)
      v(9) should be (0)
      v(5) should be (1)
    }

    "count number of adjacent wins" in {
      val v = FlagValueCalculator(Map(2 -> North, 4 -> North, 5 -> North))
      v(1) should be (1 + 0)
      v(3) should be (2 + 1)
      v(6) should be (1 + 1)
      v(7) should be (0 + 1)
    }

    "combine results from both players" in {
      val v = FlagValueCalculator(Map(2 -> North, 3 -> North, 5 -> South, 7 -> South))
      v(1) should be (1 + 0)
      v(4) should be (1 + 1)
      v(6) should be (2 + 1)
      v(8) should be (1 + 1)
    }

    "not return flags that are complete" in {
      val v = FlagValueCalculator(Map(1 -> North, 2 -> South))
      v.contains(1) should be (false)
      v.contains(2) should be (false)
      v.contains(3) should be (true)
    }
  }

  "BestCompletedFlagCalculator" should {
    "Find best possible way to complete hand of 2" in {
      val score = BestCompletedFlagCalculator(
        Set(Card("r", 4), Card("g", 10), Card("g", 7)),
        Set(Card("r", 5), Card("r", 6)))._2
      score should be (FormationCalc.score(List(Card("r", 5), Card("r", 6), Card("r", 4))))
    }

    "Find best possible way to complete hand of 0" in {
      val score = BestCompletedFlagCalculator(
        Set(Card("r", 1), Card("g", 2), Card("b", 3), Card("g", 1), Card("b", 1)),
        Set())._2
      score should be (FormationCalc.score(List(Card("r", 1), Card("g", 1), Card("b", 1))))
    }

    "Find best card to play first" in {
      val card = BestCompletedFlagCalculator(
        Set(Card("b", 3), Card("g", 1), Card("b", 1)),
        Set(Card("r", 1), Card("g", 2)))._1
      card should be (Card("b", 3))
    }
  }

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
