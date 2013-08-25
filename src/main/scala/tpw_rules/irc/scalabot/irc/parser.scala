package tpw_rules.irc.scalabot.irc

import scala.util.parsing.combinator.RegexParsers
import tpw_rules.irc.scalabot.irc.protocol.{Command, Source, Message}

/*
<message> ::= [':' <prefix> <SPACE> ] <command> <params> <crlf>
<prefix> ::= <servername> | <nick> [ '!' <user> ] [ '@' <host> ]
<command> ::= <letter> { <letter> } | <number> <number> <number>
<SPACE> ::= ' ' { ' ' }
<params> ::= <SPACE> [ ':' <trailing> | <middle> <params> ]
<middle> ::= <Any *non-empty* sequence of octets not including SPACE or NUL or CR or LF, the first of which may not be ':'>
<trailing> ::= <Any, possibly *empty*, sequence of octets not including NUL or CR or LF>
<crlf> ::= CR LF

<target> ::= <to> [ "," <target> ]
<to> ::= <channel> | <user> '@' <servername> | <nick> | <mask>
<channel> ::= ('#' | '&') <chstring>
<servername> ::= <host>
<host> ::= see RFC 952 [DNS:4] for details on allowed hostnames
<nick> ::= <letter> { <letter> | <number> | <special> }
<mask> ::= ('#' | '$') <chstring>
<chstring> ::= <any 8bit code except SPACE, BELL, NUL, CR, LF and comma (',')>

<user> ::= <nonwhite> { <nonwhite> }
<letter> ::= 'a' ... 'z' | 'A' ... 'Z'
<number> ::= '0' ... '9'
<special> ::= '-' | '[' | ']' | '\' | '`' | '^' | '{' | '}'
<nonwhite> ::= <any 8bit code except SPACE (0x20), NUL (0x0), CR (0xd), and LF (0xa)>
*/

// dis gun b complicated
object parser extends RegexParsers {
  override def skipWhitespace = false
  def apply(input: String) =
    parseAll(message, input) match {
      case Success(result, _) => result
      case f: NoSuccess => println("FAILURE", f)
    }

  lazy val message: Parser[Message] = {
    opt(":" ~> source <~ space) ~
    command ~
    params ~
    opt(" :" ~> trailing) ^^ {
      case (s ~ c) ~ p ~ t => Message(s, c, p, t)
    }
  }

  lazy val source: Parser[Source] = {
    (host ||| nick) ~ opt('!' ~> blackspace) ~ opt('@' ~> host) ^^ {
      case n~u~h => Source(n, u, h)
    }
  }

  lazy val command: Parser[Command] =
    ("""[0-9]{3}""".r | word) ^^ Command

  lazy val params = {
    opt(space ~> rep1sep(middle, " ")) ^^ {
      _.getOrElse(List())
    }
  }

  lazy val space = rep(' ')
  lazy val middle = not(':') ~> """[^\s\r\n]+""".r
  lazy val trailing = """[^\r\n]+""".r

  lazy val host = """[a-zA-Z0-9.\-^_\-\[\]\\`]+""".r
  lazy val nick = """[a-zA-Z][a-zA-Z0-9\-\[\]\|\\\`\^\{\}]*""".r

  lazy val blackspace = """[^\r\n\@]*""".r
  lazy val word = """[a-zA-Z]*""".r
  lazy val number = """[0-9]""".r
}
