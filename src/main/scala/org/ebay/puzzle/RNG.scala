package org.ebay.puzzle

import scala.math.abs

/** Simple Random Number Generator as state object.*/
trait RNG {

  /**
   * Generates next random integer and new state.
   *
   * @return integer and new state.
   */
  def nextInt: (Int, RNG)
}

/** Factory for [[RNG]] instances.*/
object RNG {

  /**
   * Simple implementation of RNG.
   *
   * @constructor creates simple RNG with seed.
   * @param seed the seed.
   */
  case class SimpleRNG(seed: Long) extends RNG {
    override def nextInt: (Int, RNG) = {
      val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL
      val nextRNG = SimpleRNG(newSeed)
      val n = (newSeed >>> 16).toInt

      (n, nextRNG)
    }
  }

  /** Holds current RNG state in application.*/
  implicit var state: RNG = SimpleRNG(System.currentTimeMillis())

  /**
   * Generates next random integer between given interval (a, b). Also, returns new state.
   *
   * @param a start of the interval.
   * @param b end of the interval.
   * @param rng current state.
   * @return integer and new state.
   */
  def choose(a: Int, b: Int)(implicit rng: RNG): (Int, RNG) = {
    require(a <= b)

    val (i, s) = rng.nextInt
    val res = (abs(i) % (b - a + 1)) + a
    state = s

    res -> s
  }

}
