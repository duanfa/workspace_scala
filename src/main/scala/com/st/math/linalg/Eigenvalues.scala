package com.st.math.linalg;

import breeze.linalg._
import breeze.linalg.eigSym.EigSym
/*
 * 求特征值和特征向量
 */

object Eigenvalues {

  def main(args: Array[String]) {

    val A = DenseMatrix((0.6165555555555555, 0.6154444444444445),
      (0.6154444444444445, 0.7165555555555555))
    val EigSym(lambda, evs) = eigSym(A)
    val es = eigSym(A)
    println(es.eigenvalues)
    println(es.eigenvectors)

  }
}

