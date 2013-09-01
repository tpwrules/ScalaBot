package tpw_rules.irc.scalabot.irc

import java.net.InetSocketAddress
import akka.actor.{ActorRef, Props, Actor}
import tpw_rules.irc.scalabot.irc.EngineMessages.{SendMessage, Initialize}
import akka.io.Tcp
import tpw_rules.irc.scalabot.ScalaBot
import akka.util.ByteString
import tpw_rules.irc.scalabot.irc.protocol._

case class ConnectionInformation(addr: InetSocketAddress, nick: String, name: String, pass: Option[String])

object Connection {
  def props(info: ConnectionInformation, parent: ActorRef) = Props(classOf[Connection], info, parent)

  val lineEnd = ByteString("\r\n")
}

// actor which handles a connection
class Connection(info: ConnectionInformation, parent: ActorRef) extends Actor {
  var tcp: ActorRef = null
  var buf: ByteString = ByteString("")

  implicit class Regex(sc: StringContext) {
    def r = new util.matching.Regex(sc.parts.mkString, sc.parts.tail.map(_ => "x"): _*)
  }

  def receive = {
    case Initialize() =>
      val tcpManager = Tcp.get(ScalaBot.system).manager
      tcpManager ! Tcp.Connect(info.addr)

    case Tcp.Connected(remote, local) =>
      tcp = sender
      tcp ! Tcp.Register(self)
      self ! SendMessage(Nick(info.nick))
      self ! SendMessage(User(info.nick, info.name))
      info.pass match {
        case Some(pass) => self ! SendMessage(Pass(pass))
        case None =>
      }
    case Tcp.Received(data) =>
      processData(buf ++ data)

    case msg: Message =>
      processMessage(msg)
    case SendMessage(msg) =>
      println("SEND", msg.byteString.utf8String)
      tcp ! Tcp.Write(msg.byteString ++ Connection.lineEnd)
  }

  def processData(data: ByteString): Unit = {
    // search for a line terminator
    val pos = data.indexOfSlice(Connection.lineEnd)
    if (pos >= 0) { // if we found one
      val msg = data.slice(0, pos).utf8String
      println(msg)
      val v = parser(msg)
      println(v)
      self ! v
      processData(data.drop(pos+2))
    } else {
      buf = data
    }
  }

  def processMessage(msg: Message) =
    msg match {
      case Ping(id) => self ! SendMessage(Pong(id))
      case Message(_, Command("001"), _, _) => self ! SendMessage(Join("#mcp-modding"))
      case Privmsg(_, chan, r"([0-9]+)$num") => self ! SendMessage(Privmsg(chan, "The number of the day is "+num))
      case _ => Unit
    }
}
