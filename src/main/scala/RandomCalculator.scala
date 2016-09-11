import scala.util.Random
import com.jhood.battlebot._

object RandomCalculator extends MoveCalculator {
  private val rand = new Random
  private val flagPositions = (1 to 9).toSet

  private def unclaimedFlags(flags: Map[Int, Direction]) = {
    flagPositions -- flags.keys.toSet
  }

  override def compute_play(hand: List[Card],
                            myFlags: Map[Int, List[Card]],
                            opponentFlags: Map[Int, List[Card]],
                            claimedFlags: Map[Int, Direction]): PlayCardResponse = {
    val card = rand.shuffle(hand).head
    val flag = rand.shuffle(unclaimedFlags(claimedFlags)).head
    PlayCardResponse(flag, card)
  }
}

