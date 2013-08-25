package tpw_rules.irc.scalabot.irc

import akka.actor.{Terminated, Props, ActorRef, Actor}
import tpw_rules.irc.scalabot.irc.EngineMessages._

object Engine {
  def props = Props(classOf[Engine])
}

class Engine extends Actor {
  var connections: List[ActorRef] = List()

  def receive = {
    case Connect(info) =>
      // create a new connection actor
      val newConn = context.actorOf(Connection.props(info, self))
      context.watch(newConn) // make sure we know when it dies
      connections = newConn :: connections
    case Shutdown() =>
      connections foreach { connection =>
        context.unwatch(connection)
        connection ! Shutdown()
      }
    case Terminated(connection) =>
      connections = connections filter { _ != connection }
  }
}
