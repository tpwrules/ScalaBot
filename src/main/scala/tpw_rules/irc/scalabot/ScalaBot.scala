package tpw_rules.irc.scalabot

import akka.actor.ActorSystem

object ScalaBot extends App {
  val system = ActorSystem("ScalaBot")

  override def main(args: Array[String]) = {
    println("hi")
  }
}
