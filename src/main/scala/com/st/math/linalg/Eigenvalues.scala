package com.st.math.linalg;

import breeze.linalg.DenseMatrix
import breeze.linalg.Vector
import breeze.linalg.eigSym
import breeze.linalg.eigSym.EigSym
import breeze.linalg.DenseVector
//import org.apache.spark.mllib.linalg.Vector
/*
 * 求特征值和特征向量
 */

object Eigenvalues {

  def main(args: Array[String]) {

    val A = DenseMatrix((3.0, 6.0, 7.0),
      (6.0, 3.0, 9.0),
      (7.0, 9.0, 1.0))
    val EigSym(lambda, evs) = eigSym(A)
    val es = eigSym(A)
    println(es.eigenvalues)
    val eigenvectors = es.eigenvectors
    println("-----------------------------------")
    println(eigenvectors)
    println("-----------------------------------")
    println(eigenvectors(0,0)*eigenvectors(0,1)+eigenvectors(1,0)*eigenvectors(1,1)+eigenvectors(2,0)*eigenvectors(2,1))
    println(eigenvectors(0,2)*eigenvectors(0,1)+eigenvectors(1,2)*eigenvectors(1,1)+eigenvectors(2,2)*eigenvectors(2,1))
    println(eigenvectors(0,0)*eigenvectors(0,2)+eigenvectors(1,0)*eigenvectors(1,2)+eigenvectors(2,0)*eigenvectors(2,2))
  }
}

