package us.elang.battleline

import com.jhood.battlebot._
import org.scalatest.{Matchers, WordSpec}

class TestHandCalculator extends WordSpec with Matchers {
  "HandCalculator" should {
    "Find best possible way to complete hand of 2" in {
      val score = HandCalculator(
        Set(Card("r", 4), Card("g", 10), Card("g", 7)),
        Set(Card("r", 5), Card("r", 6)))._2
      score should be (FormationCalc.score(List(Card("r", 5), Card("r", 6), Card("r", 4))))
    }

    "Find best possible way to complete hand of 0" in {
      val score = HandCalculator(
        Set(Card("r", 1), Card("g", 2), Card("b", 3), Card("g", 1), Card("b", 1)),
        Set())._2
      score should be (FormationCalc.score(List(Card("r", 1), Card("g", 1), Card("b", 1))))
    }

    "Find best card to play first" in {
      val card = HandCalculator(
        Set(Card("b", 3), Card("g", 1), Card("b", 1)),
        Set(Card("r", 1), Card("g", 2)))._1
      card should be (Card("b", 3))
    }
  }
}
