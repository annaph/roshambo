package org.ebay.puzzle

import RNG.state

/**
 * Represents player's choice in the game.
 *
 * Following objects use this trait:
 * [[Rock]]
 * [[Paper]]
 * [[Scissors]]
 * [[NoChoice]]
 */
sealed trait Choice

/** Represents rock choice in the game. */
case object Rock extends Choice

/** Represents scissors choice in the game. */
case object Scissors extends Choice

/** Represents paper choice in the game. */
case object Paper extends Choice

/** Represents invalid choice in the game. */
case object NoChoice extends Choice

/**
 * Represents status of the finished game between two players.
 *
 * Following objects use this trait:
 * [[Tie]]
 * [[PlayerOneWon]]
 * [[PlayerTwoWon]]
 * [[ErrorStatus]]
 */
sealed trait GameStatus

/** Game status if it was a tie between players. */
case object Tie extends GameStatus

/** Game status if first player won. */
case object PlayerOneWon extends GameStatus

/** Game status if second player won. */
case object PlayerTwoWon extends GameStatus

/** Game status if game was played with invalid choices. */
case object ErrorStatus extends GameStatus

/**
 * Represents a player in the game.
 *
 * @constructor creates a new player.
 */
abstract class Player {
  private[puzzle] var _choice: Choice = NoChoice

  /**
   * Returns player choice.
   *
   * @return player's choice.
   */
  def choice: Choice = _choice

  /**
   * Sets player's choice.
   *
   * @param choice the choice to set.
   * @return this player.
   */
  def choose(choice: Choice): Player

  /**
   * Sets player's choice randomly.
   *
   * @return this player.
   */
  def chooseRandomly(): Player

  /**
   * Plays a game between this and that player.
   *
   * @param that player to play with.
   * @return game status upon game completion.
   */
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

/**
 * Human player.
 *
 * @constructor creates a new human player.
 */
class Human extends Player {
  override def choose(choice: Choice): Player = {
    _choice = choice
    this
  }

  override def chooseRandomly(): Player =
    throw new UnsupportedOperationException("Human must make a choice by himself")
}

/**
 * Computer player.
 *
 * @constructor creates a new computer player.
 */
class Computer extends Player {
  override def choose(choice: Choice): Player =
    throw new UnsupportedOperationException("Computer cannot make a choice like humans")

  override def chooseRandomly(): Player = {
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

/** Factory for [[Player]] instances.*/
object Player {

  /**
   * Creates human player.
   *
   * @return new human player.
   */
  def Human: Player =
    new Human()

  /**
   * Creates computer player.
   *
   * @return new computer player.
   */
  def Computer: Player =
    new Computer()
}
