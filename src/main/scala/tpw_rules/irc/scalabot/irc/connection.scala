package tpw_rules.irc.scalabot.irc

import java.net.InetSocketAddress
import akka.actor.{ActorRef, Props, Actor}
import tpw_rules.irc.scalabot.irc.EngineMessages.Initialize

case class ConnectionInformation(addr: InetSocketAddress, nick: String, name: String, pass: Option[String])

object Connection {
  def props(info: ConnectionInformation, parent: ActorRef) = Props(classOf[Connection], info, parent)
}

// actor which handles a connection
class Connection(info: ConnectionInformation, parent: ActorRef) extends Actor {
  def receive = {
    case Initialize() => // do connection stuff
  }
}
