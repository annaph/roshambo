package org.ebay.puzzle

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner

import Player.Computer
import Player.Human

/**
 * Test suite for [[Player]] instances and companion object.
 */
@RunWith(classOf[JUnitRunner])
class PlayerSuite extends FunSuite {

  trait GameFixture1 {
    val player1 = Human
    val player2 = Human
  }

  trait GameFixture2 {
    val player1 = Computer
    val player2 = Computer
  }

  test("Rock vs Rock") {
    new GameFixture1 {
      player1 choose Rock
      player2 choose Rock

      val expectedGameStatus = Tie
      val actualGameStatus = player1 vs player2

      assert(actualGameStatus === expectedGameStatus, "It should be a tie")
    }
  }

  test("Rock vs Scissors") {
    new GameFixture1 {
      player1 choose Rock
      player2 choose Scissors

      val expectedGameStatus = PlayerOneWon
      val actualGameStatus = player1 vs player2

      assert(actualGameStatus === expectedGameStatus, "Player 1 should win")
    }
  }

  test("Rock vs Paper") {
    new GameFixture1 {
      player1 choose Rock
      player2 choose Paper

      val expectedGameStatus = PlayerTwoWon
      val actualGameStatus = player1 vs player2

      assert(actualGameStatus === expectedGameStatus, "Player 2 should win")
    }
  }

  test("Scissors vs Rock") {
    new GameFixture1 {
      player1 choose Scissors
      player2 choose Rock

      val expectedGameStatus = PlayerTwoWon
      val actualGameStatus = player1 vs player2

      assert(actualGameStatus === expectedGameStatus, "Player 2 should win")
    }
  }

  test("Scissors vs Scissors") {
    new GameFixture1 {
      player1 choose Scissors
      player2 choose Scissors

      val expectedGameStatus = Tie
      val actualGameStatus = player1 vs player2

      assert(actualGameStatus === expectedGameStatus, "It should be a tie")
    }
  }

  test("Scissors vs Paper") {
    new GameFixture1 {
      player1 choose Scissors
      player2 choose Paper

      val expectedGameStatus = PlayerOneWon
      val actualGameStatus = player1 vs player2

      assert(actualGameStatus === expectedGameStatus, "Player 1 should win")
    }
  }

  test("Paper vs Rock") {
    new GameFixture1 {
      player1 choose Paper
      player2 choose Rock

      val expectedGameStatus = PlayerOneWon
      val actualGameStatus = player1 vs player2

      assert(actualGameStatus === expectedGameStatus, "Player 1 should win")
    }
  }

  test("Paper vs Scissors") {
    new GameFixture1 {
      player1 choose Paper
      player2 choose Scissors

      val expectedGameStatus = PlayerTwoWon
      val actualGameStatus = player1 vs player2

      assert(actualGameStatus === expectedGameStatus, "Player 2 should win")
    }
  }

  test("Paper vs Paper") {
    new GameFixture1 {
      player1 choose Paper
      player2 choose Paper

      val expectedGameStatus = Tie
      val actualGameStatus = player1 vs player2

      assert(actualGameStatus === expectedGameStatus, "It should be a tie")
    }
  }

  test("Invalid choice vs Paper") {
    new GameFixture1 {
      player1 choose NoChoice
      player2 choose Paper

      val expectedGameStatus = ErrorStatus
      val actualGameStatus = player1 vs player2

      assert(actualGameStatus === expectedGameStatus, "Game should finish with error status")
    }
  }

  test("Random vs Random") {
    new GameFixture2 {
      player1.chooseRandomly
      player2.chooseRandomly

      val actualGameStatus = player1 vs player2

      assert(actualGameStatus != ErrorStatus, "Game should finish")
    }
  }

  test("Human must make a choice by himself") {
    new GameFixture1 {
      intercept[UnsupportedOperationException] {
        player1.chooseRandomly()
      }
    }
  }

  test("Computer cannot make a choice like humans") {
    new GameFixture2 {
      intercept[UnsupportedOperationException] {
        player1.choose(Paper)
      }
    }
  }

}
