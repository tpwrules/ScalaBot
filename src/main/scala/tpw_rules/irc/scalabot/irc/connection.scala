package tpw_rules.irc.scalabot.irc

import java.net.InetSocketAddress
import akka.actor.{ActorRef, Props, Actor}
import tpw_rules.irc.scalabot.irc.EngineMessages.Initialize
import akka.io.Tcp
import tpw_rules.irc.scalabot.ScalaBot
import akka.util.ByteString

case class ConnectionInformation(addr: InetSocketAddress, nick: String, name: String, pass: Option[String])

object Connection {
  def props(info: ConnectionInformation, parent: ActorRef) = Props(classOf[Connection], info, parent)

  val lineEnd = ByteString("\r\n")
}

// actor which handles a connection
class Connection(info: ConnectionInformation, parent: ActorRef) extends Actor {
  var tcp: ActorRef = null
  var buf: ByteString = ByteString("")

  def receive = {
    case Initialize() =>
      val tcpManager = Tcp.get(ScalaBot.system).manager
      tcpManager ! Tcp.Connect(info.addr)

    case Tcp.Connected(remote, local) =>
      tcp = sender
      tcp ! Tcp.Register(self)
    case Tcp.Received(data) =>
      processData(buf ++ data)
  }

  def processData(data: ByteString): Unit = {
    // search for a line terminator
    val pos = data.indexOfSlice(Connection.lineEnd)
    if (pos >= 0) { // if we found one
      val msg = data.slice(0, pos)
      println(msg)
      processData(msg.drop(pos+1))
    }
    buf = data
  }
}
