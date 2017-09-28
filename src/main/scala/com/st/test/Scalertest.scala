package com.st.test

import scala.reflect.runtime.universe
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.ml.feature.IDF
import org.apache.spark.ml.feature.MinMaxScaler
import org.apache.spark.ml.feature.Normalizer
import org.apache.spark.ml.feature.StandardScaler
import org.apache.spark.ml.feature.VectorIndexer
import org.apache.spark.ml.linalg.SparseVector
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.feature.MaxAbsScaler

/**
 * @author socket
 */
object Scalertest {
  def main(args: Array[String]) {
    println("useage: args(0): inputPath  args(0):recipTopN")
    val conf = new SparkConf().setAppName("RestaurantCluster").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    val array = Array[(Double, org.apache.spark.ml.linalg.Vector)](
      (1.0 -> Vectors.dense(1, 2, 3, 4)),
      (2.0 -> Vectors.dense(2, 4, 6, 8)),
      (3.0 -> Vectors.dense(9, 10, 11, 12)),
      (4.0 -> Vectors.dense(13, 14, 15, 16)),
      (5.0 -> Vectors.dense(18, 36, 54, 72)))

    val dataFrame = sqlContext.createDataFrame(array)
    dataFrame.printSchema()
    dataFrame.show()
    var after = Maxscaler(dataFrame)
    println("--------------Maxscaler------------------")
    after.map(f =>{ 
      println(f._1,f._2)
      1
    }).reduce(_+_)
    
    after = TFidfScaler(dataFrame)
    println("--------------TFidfScaler------------------")
    after.map(f =>{ 
      println(f._1,f._2)
      1
    }).reduce(_+_)
  }

  def Maxscaler(dataFrame: org.apache.spark.sql.DataFrame): RDD[(Double, org.apache.spark.ml.linalg.Vector)] = {
    val data = dataFrame.toDF("id", "features")
    val scaler = new MaxAbsScaler()
      .setInputCol("features")
      .setOutputCol("scaledFeatures")
    val scalerModel = scaler.fit(data)
    val scaledData = scalerModel.transform(data)
    scaledData.select("id", "scaledFeatures").rdd.map { x => x.getAs[Double](0) -> Vectors.dense(x.getAs[SparseVector](1).toDense.values) }
  }

  def VectorIndexer(dataFrame: org.apache.spark.sql.DataFrame): RDD[(Double, org.apache.spark.ml.linalg.Vector)] = {
    val data = dataFrame.toDF("id", "features")
    val indexer = new VectorIndexer()
      .setInputCol("features")
      .setOutputCol("scaledFeatures")
      .setMaxCategories(10);
    val indexerModel = indexer.fit(data);
    val scaledFeatures = indexerModel.transform(data)
    scaledFeatures.select("id", "scaledFeatures").rdd.map { x => x.getAs[Double](0) -> Vectors.dense(x.getAs[SparseVector](1).toDense.values) }

  }

  def normalizer(dataFrame: org.apache.spark.sql.DataFrame): RDD[(Double, org.apache.spark.ml.linalg.Vector)] = {
    val data = dataFrame.toDF("id", "features")
    val normalizer = new Normalizer()
      .setInputCol("features")
      .setOutputCol("scaledFeatures")
      .setP(1.0)
    val scaledFeatures = normalizer.transform(data)
    scaledFeatures.select("id", "scaledFeatures").rdd.map { x => x.getAs[Double](0) -> Vectors.dense(x.getAs[SparseVector](1).toDense.values) }

  }

  def StandardScaler(dataFrame: org.apache.spark.sql.DataFrame): RDD[(Double, org.apache.spark.ml.linalg.Vector)] = {
    val data = dataFrame.toDF("id", "features")
    val scaler = new StandardScaler()
      .setInputCol("features")
      .setOutputCol("scaledFeatures")
      .setWithStd(true)
      .setWithMean(false)
    val indexerModel = scaler.fit(data);
    val scaledFeatures = indexerModel.transform(data)
    scaledFeatures.select("id", "scaledFeatures").rdd.map { x => x.getAs[Double](0) -> Vectors.dense(x.getAs[SparseVector](1).toDense.values) }

  }

  def MinMaxScaler(dataFrame: org.apache.spark.sql.DataFrame): RDD[(Double, org.apache.spark.ml.linalg.Vector)] = {
    val data = dataFrame.toDF("id", "features")
    val scaler = new MinMaxScaler()
      .setInputCol("features")
      .setOutputCol("scaledFeatures")
    val indexerModel = scaler.fit(data);
    val scaledFeatures = indexerModel.transform(data)
    scaledFeatures.select("id", "scaledFeatures").rdd.map { x => x.getAs[Double](0) -> Vectors.dense(x.getAs[SparseVector](1).toDense.values) }

  }

  def TFidfScaler(dataFrame: org.apache.spark.sql.DataFrame): RDD[(Double, org.apache.spark.ml.linalg.Vector)] = {
    val data = dataFrame.toDF("id", "features")
    val idf = new IDF()
      .setInputCol("features")
      .setOutputCol("scaledFeatures")
    val indexerModel = idf.fit(data);
    val scaledFeatures = indexerModel.transform(data)
    scaledFeatures.select("id", "scaledFeatures").rdd.map { x => x.getAs[Double](0) -> Vectors.dense(x.getAs[SparseVector](1).toDense.values) }

  }
}