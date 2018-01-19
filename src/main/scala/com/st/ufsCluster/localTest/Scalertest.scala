package com.ufs.cluster.localTest

import scala.reflect.runtime.universe
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.ml.feature.IDF
import org.apache.spark.ml.feature.MaxAbsScaler
import org.apache.spark.ml.feature.MinMaxScaler
import org.apache.spark.ml.feature.Normalizer
import org.apache.spark.ml.feature.StandardScaler
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.feature.VectorIndexer
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.sql.SQLContext
import org.apache.spark.ml.feature.QuantileDiscretizer

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
      (2.0 -> Vectors.dense(2, -3, 4, 8)),
      (3.0 -> Vectors.dense(-1, 0, 5, 0)),
      (4.0 -> Vectors.dense(0, 1, 6, 8)),
      (5.0 -> Vectors.dense(18, 36, 7, 72)))

    val dataFrame = sqlContext.createDataFrame(array)
    dataFrame.printSchema()
    dataFrame.show()
    var after = MaxAbsScaler(dataFrame)
    println("--------------MaxAbsScaler------------------")
    after.show(false)

    after = VectorIndexer(dataFrame)
    println("--------------VectorIndexer------------------")
    after.show(false)

    after = normalizer(dataFrame)
    println("--------------normalizer------------------")
    after.show(false)

    after = StandardScaler(dataFrame)
    println("--------------StandardScaler------------------")
    after.show(false)

    after = MinMaxScaler(dataFrame)
    println("--------------MinMaxScaler------------------")
    after.show(false)

    after = TFidfScaler(dataFrame)
    println("--------------TFidfScaler------------------")
    after.show(false)
  }

  def MaxAbsScaler(dataFrame: org.apache.spark.sql.DataFrame): org.apache.spark.sql.DataFrame = {
    val data = dataFrame.toDF("id", "features")
    val scaler = new MaxAbsScaler()
      .setInputCol("features")
      .setOutputCol("scaledFeatures")
    val scalerModel = scaler.fit(data)
    // rescale each feature to range [-1, 1]
    val scaledData = scalerModel.transform(data)
    scaledData.select("id", "features", "scaledFeatures")
  }

  def VectorIndexer(dataFrame: org.apache.spark.sql.DataFrame): org.apache.spark.sql.DataFrame = {
    val data = dataFrame.toDF("id", "features")
    val indexer = new VectorIndexer()
      .setInputCol("features")
      .setOutputCol("scaledFeatures")
      .setMaxCategories(4);
    val indexerModel = indexer.fit(data);
    val scaledFeatures = indexerModel.transform(data)
    scaledFeatures.select("id", "features", "scaledFeatures")

  }

  def normalizer(dataFrame: org.apache.spark.sql.DataFrame): org.apache.spark.sql.DataFrame = {
    val data = dataFrame.toDF("id", "features")
    val normalizer = new Normalizer()
      .setInputCol("features")
      .setOutputCol("scaledFeatures")
      .setP(1.0)
    val scaledFeatures = normalizer.transform(data)
    scaledFeatures.select("id", "features", "scaledFeatures")

  }

  def StandardScaler(dataFrame: org.apache.spark.sql.DataFrame): org.apache.spark.sql.DataFrame = {
    val data = dataFrame.toDF("id", "features")
    val scaler = new StandardScaler()
      .setInputCol("features")
      .setOutputCol("scaledFeatures")
      .setWithStd(true)
      .setWithMean(false)
    val indexerModel = scaler.fit(data);
    val scaledFeatures = indexerModel.transform(data)
    scaledFeatures.select("id", "features", "scaledFeatures")

  }

  def MinMaxScaler(dataFrame: org.apache.spark.sql.DataFrame): org.apache.spark.sql.DataFrame = {
    val data = dataFrame.toDF("id", "features")
    val scaler = new MinMaxScaler()
      .setInputCol("features")
      .setOutputCol("scaledFeatures")
    val indexerModel = scaler.fit(data);
    val scaledFeatures = indexerModel.transform(data)
    scaledFeatures.select("id", "features", "scaledFeatures")

  }

  def TFidfScaler(dataFrame: org.apache.spark.sql.DataFrame): org.apache.spark.sql.DataFrame = {
    val data = dataFrame.toDF("id", "features")
    val idf = new IDF()
      .setInputCol("features")
      .setOutputCol("scaledFeatures")
    val indexerModel = idf.fit(data);
    val scaledFeatures = indexerModel.transform(data)
    scaledFeatures.select("id", "features", "scaledFeatures")

  }
  def QuantileDiscretizer(dataFrame: org.apache.spark.sql.DataFrame): org.apache.spark.sql.DataFrame = {

    //val data = Array((0, 18.0), (1, 19.0), (2, 8.0), (3, 5.0), (4, 2.2))
    //var df = spark.createDataFrame(data).toDF("id", "hour")
    //
    //val discretizer = new QuantileDiscretizer()
    //  .setInputCol("hour")
    //  .setOutputCol("result")
    //  .setNumBuckets(3)
    //
    //val result = discretizer.fit(df).transform(df)
    //result.show()

    val data = dataFrame.toDF("id", "features")
    val discretizer = new QuantileDiscretizer()
      .setInputCol("hour")
      .setOutputCol("result")
      .setNumBuckets(3)
    val indexerModel = discretizer.fit(data);
    val scaledFeatures = indexerModel.transform(data)
    scaledFeatures.select("id", "features", "scaledFeatures")

  }
  def VectorAssembler(dataFrame: org.apache.spark.sql.DataFrame): org.apache.spark.sql.DataFrame = {

    //val dataset = spark.createDataFrame(
    //  Seq((0, 18, 1.0, Vectors.dense(0.0, 10.0, 0.5), 1.0))
    //).toDF("id", "hour", "mobile", "userFeatures", "clicked")
    //
    //val assembler = new VectorAssembler()
    //  .setInputCols(Array("hour", "mobile", "userFeatures"))
    //  .setOutputCol("features")
    //
    //val output = assembler.transform(dataset)
    //println(output.select("features", "clicked").first())

    val data = dataFrame.toDF("id", "features")
    val idf = new IDF()
      .setInputCol("features")
      .setOutputCol("scaledFeatures")

    val assembler = new VectorAssembler()
      .setInputCols(Array("hour", "mobile", "userFeatures"))
      .setOutputCol("features")

    val scaledFeatures = assembler.transform(data)
    scaledFeatures.select("id", "features", "scaledFeatures")

  }
}