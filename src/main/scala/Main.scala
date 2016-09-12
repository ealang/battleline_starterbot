import bot.ExpValueCardCalculator
import com.jhood.battlebot.{StdMsgStrategyWrapper, CalculatedStrategy}

import scala.io.StdIn.readLine

object Main extends App {
  val wrappedStrategy = new StdMsgStrategyWrapper(new CalculatedStrategy("RandoBot", ExpValueCardCalculator()))

  while(true) {
    val response = wrappedStrategy.update(readLine())
    for (line <- response) {
      println(line)
    }
  }
}
