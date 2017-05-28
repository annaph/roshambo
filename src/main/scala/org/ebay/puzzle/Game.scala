package org.ebay.puzzle

import Player.Computer
import Player.Human
import scalaz._
import scalaz.effect.ST
import scalaz.effect.ST.newVar
import scalaz.effect.ST.runST

object Game {

  type GameInfo = (Choice, Choice, GameStatus)
  type ForallST[A] = Forall[({ type f[S] = ST[S, A] })#f]

  def runComputerVsComputer(): GameInfo = runST(new ForallST[GameInfo] {
    def apply[S] = computerVsComputer()
  })

  def runHumanVsComputer(choice: Choice): GameInfo = runST(new ForallST[GameInfo] {
    def apply[S] = humanVsComputer(choice)
  })

  private def computerVsComputer[S](): ST[S, GameInfo] =
    for {
      player1Ref <- newVar(Computer)
      player2Ref <- newVar(Computer)
      _ <- player1Ref mod { _.chooseRandomly() }
      _ <- player2Ref mod { _.chooseRandomly() }
      player1 <- player1Ref.read
      player2 <- player2Ref.read
      gameStatus = player1 vs player2
    } yield (player1.choice, player2.choice, gameStatus)

  private def humanVsComputer[S](humanChoice: Choice): ST[S, GameInfo] =
    for {
      player1Ref <- newVar(Human)
      player2Ref <- newVar(Computer)
      _ <- player1Ref mod { _.choose(humanChoice) }
      _ <- player2Ref mod { _.chooseRandomly() }
      player1 <- player1Ref.read
      player2 <- player2Ref.read
      gameStatus = player1 vs player2
    } yield (player1.choice, player2.choice, gameStatus)

}
