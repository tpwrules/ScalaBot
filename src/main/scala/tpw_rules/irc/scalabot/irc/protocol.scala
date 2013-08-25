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
}
