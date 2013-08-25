package tpw_rules.irc.scalabot.irc

import akka.util.{ByteStringBuilder, ByteString}

object protocol {
  val SP = ByteString(" ")

  sealed trait ProtocolMessage {
    val byteString: ByteString
  }

  sealed trait SimpleProtocolMessage {
    val value: String
    val byteString = ByteString(value)
  }

  case class Source(from: String, user: Option[String], server: Option[String]) extends ProtocolMessage {
    val byteString = ByteString(List(from, user.getOrElse(""), server.getOrElse("")).mkString(" "))
  }

  case class Command(value: String) extends SimpleProtocolMessage

  case class Message(source: Option[Source], command: Command, params: List[String], data: Option[String]) extends ProtocolMessage {
    val byteString = (new ByteStringBuilder ++=
      source.map { _.byteString }.getOrElse(ByteString("")) ++=
      command.byteString ++= protocol.SP ++=
      ByteString(params.mkString(" ")) ++=
      ByteString(data.map { " :" + _ } getOrElse "")).result()
  }

  object Ping {
    def apply(id: String) =
      Message(None, Command("PING"), List(id), None)
    def unapply(msg: Message) =
      msg match {
        case Message(_, Command("PING"), List(id), _) => Some(id)
        case _ => None
      }
  }

  object Pong {
    def apply(id: String) =
      Message(None, Command("PONG"), List(id), None)
    def unapply(msg: Message) =
      msg match {
        case Message(_, Command("PONG"), List(id), None) => Some(id)
        case _ => None
      }
  }

  object Nick {
    def apply(nick: String) =
      Message(None, Command("NICK"), List(nick), None)
    def unapply(msg: Message) =
      msg match {
        case Message(_, Command("NICK"), List(nick), None) => Some(nick)
        case _ => None
      }
  }

  object Pass {
    def apply(pass: String) =
      Message(None, Command("PASS"), List(pass), None)
    def unapply(msg: Message) =
      msg match {
        case Message(_, Command("PASS"), List(pass), None) => Some(pass)
        case _ => None
      }
  }

  object User {
    def apply(nick: String, realName: String) =
      Message(None, Command("USER"), List(nick, "0", "0"), Some(realName))
    def unapply(msg: Message) =
      msg match {
        case Message(_, Command("USER"), List(nick, _, _), realName) => Some((nick, realName))
      }
  }
}