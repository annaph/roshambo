package org.ebay.puzzle

import RNG.state

sealed trait Choice
case object Rock extends Choice
case object Scissors extends Choice
case object Paper extends Choice
case object NoChoice extends Choice

sealed trait GameStatus
case object Tie extends GameStatus
case object PlayerOneWon extends GameStatus
case object PlayerTwoWon extends GameStatus
case object ErrorStatus extends GameStatus

abstract class Player {
  private[puzzle] var _choice: Choice = NoChoice

  def choice: Choice = _choice

  def choose(choice: Choice): Player
  def chooseRandomly(): Player

  def vs(that: Player): GameStatus =
    (_choice, that.choice) match {
      case (Rock, Rock) | (Scissors, Scissors) | (Paper, Paper) =>
        Tie
      case (Rock, Scissors) | (Scissors, Paper) | (Paper, Rock) =>
        PlayerOneWon
      case (Rock, Paper) | (Scissors, Rock) | (Paper, Scissors) =>
        PlayerTwoWon
      case _ =>
        ErrorStatus
    }
}

private class Human extends Player {
  override def choose(choice: Choice): Player = {
    _choice = choice
    this
  }

  def chooseRandomly(): Player =
    throw new UnsupportedOperationException("Human must make a choice by himself")
}

private class Computer extends Player {
  override def choose(choice: Choice): Player =
    throw new UnsupportedOperationException("Computer cannot make a choice like humans")

  def chooseRandomly(): Player = {
    RNG.choose(1, 3)._1 match {
      case 1 =>
        _choice = Paper
      case 2 =>
        _choice = Rock
      case 3 =>
        _choice = Scissors
    }

    this
  }
}

object Player {
  def Human: Player =
    new Human()

  def Computer: Player =
    new Computer()
}
