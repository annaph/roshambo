package org.ebay.puzzle

import scala.math.abs

trait RNG {
  def nextInt: (Int, RNG)
}

object RNG {
  case class SimpleRNG(seed: Long) extends RNG {
    override def nextInt: (Int, RNG) = {
      val newSeed = (seed * 0x5DEECE66DL + 0xBL) & 0xFFFFFFFFFFFFL
      val nextRNG = SimpleRNG(newSeed)
      val n = (newSeed >>> 16).toInt

      (n, nextRNG)
    }
  }

  implicit var state: RNG = SimpleRNG(System.currentTimeMillis())

  def choose(a: Int, b: Int)(implicit rng: RNG): (Int, RNG) = {
    require(a <= b)

    val (i, s) = rng.nextInt
    val res = (abs(i) % (b - a + 1)) + a
    state = s

    res -> s
  }

}
