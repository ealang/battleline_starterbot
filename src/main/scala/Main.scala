import com.jhood.battlebot._

import scala.io.StdIn.readLine

object Main extends App {
  val wrappedStrategy = new StdMsgStrategyWrapper(new CalculatedStrategy("RandoBot", RandomCalculator))

  while(true) {
    val response = wrappedStrategy.update(readLine())
    println(response.getOrElse(""))
  }
}
