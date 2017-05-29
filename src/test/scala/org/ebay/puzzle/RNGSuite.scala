package org.ebay.puzzle

import org.ebay.puzzle.RNG.SimpleRNG
import org.ebay.puzzle.RNG.choose
import org.junit.runner.RunWith
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Prop
import org.scalacheck.Prop.BooleanOperators
import org.scalacheck.Prop.forAll
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

/**
 * Test suite for [[RNG]] instances and companion object.
 */
@RunWith(classOf[JUnitRunner])
class RNGSuite extends FunSuite with Checkers {

  implicit val arSimpleRNG: Arbitrary[SimpleRNG] = Arbitrary {
    for {
      seed <- arbitrary[Long]
    } yield SimpleRNG(seed)
  }

  test("Generate a random integer for given RNG") {
    val rng = SimpleRNG(7)

    val expected = 2693242
    val actual: Int = rng.nextInt._1

    assert(actual === expected, "Generated random integer is wrong")
  }

  test("Generate 3 random integer for given RNG") {
    val rng = SimpleRNG(7)

    val expected = List(2693242, 505774798, -623542375)

    val (number1, state1) = rng.nextInt
    val (number2, state2) = state1.nextInt
    val (number3, _) = state2.nextInt

    val actual = List(number1, number2, number3)

    assert(actual === expected, "Generated random integers are wrong")
  }

  test("Generate a random integer between 1 and 3 for given RNG") {
    val rng = SimpleRNG(7)

    val expected = 2
    val actual: Int = choose(1, 3)(rng)._1

    assert(actual === expected, "Generated random integer is wrong")
  }

  test("Generate a random integer between arbitrary range") {
    val propGenInt: Prop = forAll { (a: Int, b: Int) =>
      (a > -10000 && a < 10000) ==> {
        try {
          val (res, newState) = choose(a, b)
          (res >= a
            && res <= b
            && RNG.state.asInstanceOf[SimpleRNG].seed == newState.asInstanceOf[SimpleRNG].seed)
        } catch {
          case _: Throwable =>
            b < a
        }
      }
    }

    check(propGenInt)
  }

  test("Generate a random integer with arbitrary RNG") {
    val propGenInt: Prop = forAll { (rng: SimpleRNG) =>
      val res: Int = choose(1, 3)(rng)._1
      res >= 1 && res <= 3
    }

    check(propGenInt)
  }

}
