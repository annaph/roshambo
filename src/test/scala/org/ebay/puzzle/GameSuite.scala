package org.ebay.puzzle

import org.junit.runner.RunWith
import org.scalacheck.Gen
import org.scalacheck.Prop
import org.scalacheck.Prop.forAll
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

import Game.runComputerVsComputer
import Game.runHumanVsComputer

@RunWith(classOf[JUnitRunner])
class GameActionsSuite extends FunSuite with Checkers {

  implicit val genChoice: Gen[Choice] = Gen.frequency(
    (3, Rock),
    (3, Paper),
    (3, Scissors),
    (1, NoChoice))

  test("Game between 2 Computers") {
    val (computer1Choice, computer2Choice, gameStatus) = runComputerVsComputer()

    assert(computer1Choice != NoChoice && computer2Choice != NoChoice,
      "Computers should select valid choices")

    assert(gameStatus != ErrorStatus, "Game should finish with valid status")
  }

  test("Game between Human and Computer") {
    val propHumanVsComputer: Prop = forAll(genChoice) { (choice) =>
      val (humanCoice, computerChoice, gameStatus) = runHumanVsComputer(choice)

      humanCoice == choice && computerChoice != NoChoice

      if (choice != NoChoice) {
        gameStatus != ErrorStatus
      } else {
        gameStatus == ErrorStatus
      }
    }

    check(propHumanVsComputer)
  }

}
