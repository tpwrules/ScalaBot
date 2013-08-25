package tpw_rules.irc.scalabot.irc

import akka.actor.{Props, ActorRef, Actor}
import tpw_rules.irc.scalabot.irc.messages._

object Engine {
  def props = Props(classOf[Engine])
}

class Engine extends Actor {
  var connections: List[ActorRef] = List()

  def receive = {
    case Shutdown() =>
      connections foreach { connection =>
        context.unwatch(connection)
        connection ! Shutdown()
      }
    case _ => None
  }

}
