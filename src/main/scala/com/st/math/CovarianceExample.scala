package com.st.math

import org.apache.spark.mllib.linalg.distributed.RowMatrix
import org.apache.spark.mllib.linalg.{ Matrix, Vector, Vectors }
import org.apache.spark.mllib.stat.MultivariateStatisticalSummary
import org.apache.spark.sql.{ Row, SQLContext }
import org.apache.spark.{ SparkContext, SparkConf }
import org.apache.spark.sql.SparkSession

object CovarianceExample {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("CovarianceExample").setMaster("local[8]")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    val spark = SparkSession.builder().config(conf).getOrCreate()

    import spark.implicits._

    //输入数据
    val data = Array(
      Vectors.dense(4.0, 2.0, 3.0),
      Vectors.dense(5.0, 6.0, 1.0),
      Vectors.dense(2.0, 4.0, 7.0),
      Vectors.dense(3.0, 6.0, 5.0))
    val data2 = Array(
      Vectors.dense(0.69, 0.49),
      Vectors.dense(-1.31, -1.21),
      Vectors.dense(0.39, 0.99),
      Vectors.dense(0.09, 0.29),
      Vectors.dense(1.29, 1.09),
      Vectors.dense(0.49, 0.79),
      Vectors.dense(0.19, -0.31),
      Vectors.dense(-0.81, -0.81),
      Vectors.dense(-0.31, -0.31),
      Vectors.dense(-0.71, -1.01))

    // Array[Vector]转换成DataFrame
      val df = sqlContext.createDataFrame(data.map(Tuple1.apply)).toDF("features")

    // DataFrame转换成RDD
    //    implicit val aEncoder = org.apache.spark.sql.Encoders.kryo[Vector]

    //    val rddData = df.select("features").map { case Row(v: Vector) => v }.rdd
    val rddData = df.select("features").map {  case Row(v: Vector) => v }(org.apache.spark.sql.Encoders.kryo[Vector]).rdd

    // RDD转换成RowMatrix
    val mat: RowMatrix = new RowMatrix(rddData)

    // 统计
    val stasticSummary: MultivariateStatisticalSummary = mat.computeColumnSummaryStatistics()

    // 均值
    println(stasticSummary.mean)
    // 结果：3.5,4.5,4.0

    // 方差
    println(stasticSummary.variance)
    // 结果：1.6666666666666667,3.6666666666666665,6.666666666666667

    // 协方差
    val covariance: Matrix = mat.computeCovariance()
    println(covariance)
    // 结果：
    //  cov(dim1,dim1) cov(dim1,dim2) cov(dim1,dim3)
    //  cov(dim2,dim1) cov(dim2,dim2) cov(dim2,dim3)
    //  cov(dim3,dim1) cov(dim3,dim2) cov(dim3,dim3)
    //  1.6666666666666679   0.3333333333333357   -3.3333333333333304
    //  0.3333333333333357   3.666666666666668    -0.6666666666666679
    //  -3.3333333333333304  -0.6666666666666679  6.666666666666668
    // 结果分析：以cov(dim1,dim2)为例
    //  dim1均值：3.5  dim2均值：4.5
    //  val cov(dim2,dim3)=((4.0-3.5)*(2.0-4.5)+(5.0-3.5)*(6.0-4.5)+(2.0-3.5)*(4.0-4.5)+(3.0-3.5)*(6.0-4.5))/(4-1)
    //  cov(dim2,dim3)=0.3333333333333333 
  }

}