package org.ebay.puzzle

import Game.runComputerVsComputer
import Game.runHumanVsComputer
import scalaz.effect.IO
import scalaz.effect.IO.ioUnit
import scalaz.effect.IO.putStr
import scalaz.effect.IO.putStrLn
import scalaz.effect.IO.readLn

object SimpleUI {
  import Messages._

  def runUI(): Unit =
    createUI.unsafePerformIO()

  private lazy val createUI: IO[Unit] =
    for {
      _ <- helpIO
      _ <- mainIO
      _ <- goodbyeIO
    } yield ()

  private def helpIO: IO[Unit] =
    for {
      _ <- putStrLn(helpString)
    } yield ()

  private def mainIO: IO[Unit] =
    for {
      _ <- doWhile(readLn) { line =>
        when[Unit](line != "q") {
          for {
            _ <- line match {
              case "1" =>
                humanVsComputerIO
              case "2" =>
                computerVsComputerIO
              case _ =>
                putStrLn(errorMsg)
            }
            _ <- putStrLn(chooseGameType)
          } yield ()
        }
      }
    } yield ()

  private def goodbyeIO: IO[Unit] =
    for {
      _ <- putStr(goodbyeMsg)
    } yield ()

  private def humanVsComputerIO: IO[Unit] =
    for {
      _ <- putStrLn(humanVsComputer)
      _ <- putStrLn(enterChoice)
      choiceStr <- readLn
      choice <- IO {
        choiceStr match {
          case "r" =>
            Rock
          case "p" =>
            Paper
          case "s" =>
            Scissors
          case _ =>
            NoChoice
        }
      }
      gameInfo <- IO { runHumanVsComputer(choice) }
      _ <- putStrLn(humanChoice + gameInfo._1)
      _ <- putStrLn(computerChoice + gameInfo._2)
      _ <- gameInfo._3 match {
        case PlayerOneWon =>
          putStrLn(humanWon)
        case PlayerTwoWon =>
          putStrLn(computerWon)
        case Tie =>
          putStrLn(tie)
        case _ =>
          putStrLn(errorMsg)
      }
    } yield ()

  private def computerVsComputerIO: IO[Unit] =
    for {
      _ <- putStrLn(computer1VsComputer2)
      gameInfo <- IO { runComputerVsComputer() }
      _ <- putStrLn(computer1Choice + gameInfo._1)
      _ <- putStrLn(computer2Choice + gameInfo._2)
      _ <- gameInfo._3 match {
        case PlayerOneWon =>
          putStrLn(computer1Won)
        case PlayerTwoWon =>
          putStrLn(computer2Won)
        case Tie =>
          putStrLn(tie)
        case _ =>
          putStrLn(errorMsg)
      }
    } yield ()

  private def doWhile[A](a: IO[A])(cond: A => IO[Boolean]): IO[Unit] =
    for {
      a1 <- a
      ok <- cond(a1)
      _ <- if (ok) {
        doWhile(a)(cond)
      } else {
        ioUnit
      }
    } yield ()

  private def when[A](b: Boolean)(a: => IO[A]): IO[Boolean] =
    if (b) {
      a.map { _ => true }
    } else {
      IO { false }
    }
}

object Messages {
  val helpString: String = """
  | The Amazing Rock–paper–scissors, v1.0.0
  | 1 - play Human vs Computer
  | 2 - play Computer vs Computer
  | q - quit
  |
  | To select specific shape use following commands:
  | p - paper
  | r - rock
  | s - scissors
  |
  | Choose game type (1 or 2) or (q)uit:
  """.trim.stripMargin

  val chooseGameType = "Choose game type (1 or 2) or (q)uit"
  val computer1VsComputer2 = "Computer 1 vs Computer 2:"
  val humanVsComputer = "Human vs Computer:"
  val computer1Choice = "Computer 1 choice: "
  val computer2Choice = "Computer 2 choice: "
  val humanChoice = "Your choice: "
  val computerChoice = "Computer choice: "
  val computer1Won = "Computer 1 won"
  val computer2Won = "Computer 2 won"
  val humanWon = "You won"
  val computerWon = "Computer won"
  val tie = "It's a tie"
  val errorMsg = "Error occurred while playing game! Check your choice"
  val enterChoice = "Enter you choice: "
  val goodbyeMsg = "Thanks for playing\n"
}
