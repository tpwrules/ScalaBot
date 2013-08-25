package tpw_rules.irc.scalabot.irc


sealed trait IRCMessage

object EngineMessages {

  case class Shutdown() extends IRCMessage

  case class Connect(info: ConnectionInformation) extends IRCMessage

  case class Initialize() extends IRCMessage
}

object ConnectionMessages {
  case class Connect() extends IRCMessage
}