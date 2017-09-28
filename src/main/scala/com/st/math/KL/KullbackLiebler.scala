package com.st.math.KL

import java.lang.Math._

object KullbackLiebler {
  final val EPS = 1e-10
  type DATASET = Iterator[(Double, Double)]

  def execute(xy: DATASET, f: Double => Double): Double = {

    val z = xy.filter {
      case (x, y) => abs(y) > EPS
    }
    -z./:(0.0) {
      case (s, (x, y)) => {
        val px = f(x)
        s + px * Math.log(px / y)
      }
    }
  }

  def execute(xy: DATASET, fs: Iterable[Double => Double]): Iterable[Double] = {
    fs.map(execute(xy, _))
  }

  def main(args: Array[String]): Unit = {
    val list = List((11.0 / 13, 2.0 / 13), (10.0 / 11, 1.0 / 11)).iterator
    println(execute(list, f(_)))
  }
  def f(d: Double): Double = {
    return d;
  }
}