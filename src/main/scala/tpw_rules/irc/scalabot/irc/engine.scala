package tpw_rules.irc.scalabot.irc

import akka.actor.{Props, ActorRef, Actor}

object Engine {
  def props = Props(classOf[Engine])
}

class Engine extends Actor {
  var connections: List[ActorRef] = List()

  def receive = {
    case _ => None
  }

}
