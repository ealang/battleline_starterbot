import scala.io.StdIn.readLine

import com.jhood.battlebot._

class Calculator extends MoveCalculator {
  override def compute_play(hand: List[Card],
                            myFlags: Map[Int, List[Card]],
                            opponentFlags: Map[Int, List[Card]],
                            claimedFlags: Map[Int, Direction]): PlayCardResponse = {
    PlayCardResponse(1, Card("color1",1))
  }
}

object Main extends App {
  val wrappedStrategy = new StdMsgStrategyWrapper(new CalculatedStrategy("testbot", new Calculator))

  while(true) {
    val response = wrappedStrategy.update(readLine())
    response.map{ println(_) }
  }
}
