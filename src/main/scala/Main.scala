import bot.ExpValueCardCalculator
import com.jhood.battlebot.{StdMsgStrategyWrapper, CalculatedStrategy}

import scala.io.StdIn.readLine

object Main extends App {
  if (args.length == 1) {
    val name = args(0)
    val wrappedStrategy = new StdMsgStrategyWrapper(new CalculatedStrategy(name, ExpValueCardCalculator()))

    while(true) {
      val response = wrappedStrategy.update(readLine())
      for (line <- response) {
        println(line)
      }
    }
  } else {
    println("usage: <bot name>")
  }
}
