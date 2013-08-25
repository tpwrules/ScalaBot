package tpw_rules.irc.scalabot

import akka.actor.ActorSystem
import tpw_rules.irc.scalabot.irc.{Engine, ConnectionInformation}
import java.net.InetSocketAddress
import tpw_rules.irc.scalabot.irc.EngineMessages.Connect

object ScalaBot extends App {
  lazy val system = ActorSystem("ScalaBot")
  lazy val engine = system.actorOf(Engine.props)

  override def main(args: Array[String]) = {
    println("hi")
    val v = ConnectionInformation(new InetSocketAddress("irc.efnet.org", 6667), "olol", "lolo", None)
    engine ! Connect(v)
  }
}
