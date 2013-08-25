package tpw_rules.irc.scalabot.irc


object messages {
  sealed trait IRCMessage

  case class Shutdown() extends IRCMessage
}
