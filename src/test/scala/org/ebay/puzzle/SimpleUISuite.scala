package org.ebay.puzzle

import java.io.ByteArrayOutputStream
import java.io.StringReader

import org.junit.runner.RunWith
import org.scalatest.FunSpecLike
import org.scalatest.Matchers
import org.scalatest.junit.JUnitRunner

import SimpleUI.runUI

/**
 * Test suite for [[SimpleUI]] singleton object.
 */
@RunWith(classOf[JUnitRunner])
class SimpleUISuite extends FunSpecLike with Matchers {

  describe("Rock–paper–scissors game") {

    it("should quit after selecting option 'q'") {
      val output = new ByteArrayOutputStream()
      Console.withIn(new StringReader("q")) {
        Console.withOut(output) {
          runUI()
        }
      }

      output.toString should include regex """Thanks for playing"""
    }

    it("should show game between two Computers after selecting game type '2'") {
      val output = new ByteArrayOutputStream()
      Console.withIn(new StringReader("2\nq")) {
        Console.withOut(output) {
          runUI()
        }
      }

      val outputString = output.toString

      outputString should include regex """Computer 1 choice:"""
      outputString should include regex """Computer 2 choice:"""
      outputString should include regex """[Computer [1|2] won] | It's a tie"""
    }

    it("should show game between Human and Computer after selecting game type '1'") {
      val output = new ByteArrayOutputStream()
      Console.withIn(new StringReader("1\np\nq")) {
        Console.withOut(output) {
          runUI()
        }
      }

      val outputString = output.toString

      outputString should include regex """Your choice:"""
      outputString should include regex """Computer choice:"""
      outputString should include regex """[[You|Computer] won] | It's a tie"""
    }

    it("should show game between Human and Computer and then between 2 Computers after selecting game types '1' and then '2'") {
      val output = new ByteArrayOutputStream()
      Console.withIn(new StringReader("1\np\n2\nq")) {
        Console.withOut(output) {
          runUI()
        }
      }

      val outputString = output.toString

      outputString should include regex """Your choice:"""
      outputString should include regex """Computer choice:"""
      outputString should include regex """[[You|Computer] won] | It's a tie"""

      outputString should include regex """Computer 1 choice:"""
      outputString should include regex """Computer 2 choice:"""
      outputString should include regex """[Computer [1|2] won] | It's a tie"""
    }
  }

}
