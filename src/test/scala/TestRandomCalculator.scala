import com.jhood.battlebot._
import org.scalatest.{Matchers, WordSpec}

class TestRandomCalculator extends WordSpec with Matchers {

  val allFlags: Map[Int, Direction] = (1 to 9).map(_ -> South).toMap

  "A RandomCalculator" should {
    "Pick only flag available" in {
      val play = RandomCalculator.compute_play(
        hand=List(Card("red", 1), Card("blue", 2)),
        myFlags=Map.empty,
        opponentFlags=Map.empty,
        claimedFlags=allFlags - 5)
      play.flag should equal (5)
    }

    "Pick only card available" in {
      val play = RandomCalculator.compute_play(
        hand=List(Card("red", 1)),
        myFlags=Map.empty,
        opponentFlags=Map.empty,
        claimedFlags=Map.empty)
      play.card should equal (Card("red", 1))
    }
  }
}
