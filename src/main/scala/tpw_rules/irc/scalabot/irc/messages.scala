package tpw_rules.irc.scalabot.irc

import tpw_rules.irc.scalabot.irc.protocol.Message

sealed trait IRCMessage

object EngineMessages {

  case class Shutdown() extends IRCMessage

  case class Connect(info: ConnectionInformation) extends IRCMessage

  case class Initialize() extends IRCMessage

  case class SendMessage(msg: Message) extends IRCMessage
}

object ConnectionMessages {
  case class Connect() extends IRCMessage
}