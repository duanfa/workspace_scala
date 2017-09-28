package com.st.test

import com.huaban.analysis.jieba.JiebaSegmenter
import com.huaban.analysis.jieba.JiebaSegmenter.SegMode
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import scala.io.Source

/**
 * @author socket
 */
object test {
  def main(args: Array[String]): Unit = {
    val list = List(1, 2, 3, 4)
    val r = list./:(1) {
      case (a, b) => {
        a + b
      }
    }
    println(r)
  }
}