package bot

import bot._
import com.jhood.battlebot._
import org.scalatest.{Matchers, WordSpec}

class TestExpValueCardCalculator extends WordSpec with Matchers {

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

  "ExpValueCardCalculator" should {

    "Always give best card to most valuable flag" in {
      val response = ExpValueCardCalculator().compute_play(
        myDirection=North,
        myHand=List(Card("s1", 4), Card("s2", 5)),
        myFlags=Map(),
        opponentFlags=Map(),
        claimedFlags=Map(1 -> North))

      response should be (PlayCardResponse(2, Card("s2", 5)))
    }

    "Always pick highest formation" in {
      val response = ExpValueCardCalculator().compute_play(
        myDirection=North,
        myHand=List(Card("s1", 8), Card("s2", 1)),
        myFlags=Map(2 -> List(Card("s2", 2), Card("s2", 3))),
        opponentFlags=Map(),
        claimedFlags=Map(1 -> North))

      response should be (PlayCardResponse(2, Card("s2", 1)))
    }

    "Not try to play to a flag that is full" in {
      val response = ExpValueCardCalculator().compute_play(
        myDirection=North,
        myHand=List(Card("s1", 4), Card("s2", 5)),
        myFlags=Map(2 -> List(Card("s1", 1), Card("s2", 2), Card("s3", 3))),
        opponentFlags=Map(),
        claimedFlags=Map(1 -> North, 3 -> North))

      response should be (PlayCardResponse(4, Card("s2", 5)))
    }
  }
}
