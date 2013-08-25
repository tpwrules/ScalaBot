package tpw_rules.irc.scalabot

import akka.actor.ActorSystem

object ScalaBot extends App {
  val system = ActorSystem("")

  override def main(args: Array[String]) = {
    println("hi")
  }
}
