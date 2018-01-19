package com.ufs.cluster.mllib

import scala.collection.immutable.Vector
import org.apache.spark.SparkContext
import org.apache.spark.ml.feature.MaxAbsScaler
import org.apache.spark.ml.linalg.SparseVector
import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.feature.VectorTransformer
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.Dataset
import org.apache.spark.sql.Row
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.types.StructType
import org.apache.spark.sql.types.LongType
import org.apache.spark.ml.feature.VectorIndexer
import org.apache.spark.ml.feature.Normalizer
import org.apache.spark.ml.feature.StandardScaler
import org.apache.spark.ml.feature.MinMaxScaler
import org.apache.spark.ml.feature.IDF

/**
 * @author socket
 */
object RestaurantClusterKmeans {
  def main(args: Array[String]) {

    var initK = 6
    var maxIterations = 100
    var inputPath = "/user/hadoop/ufsCluster/ufs.libsvm"
    var postfix = ""
    var scaler = "default"
    println("useage: args(0): initK  args(1):maxIterations args(2):inputPath args(3):postfix  args(4):scaler")
    if (args.size > 0) {
      initK = args(0).toInt
    }
    if (args.size > 1) {
      maxIterations = args(1).toInt
    }
    if (args.size > 2) {
      inputPath = args(2)
    }
    if (args.size > 3) {
      postfix = args(3)
    }
    if (args.size > 4) {
    	scaler = args(4)
    }
    println("params: initK:" + initK + " maxIterations:" + maxIterations + " inputPath:" + inputPath + postfix + " postfix:" + postfix + " scaler:"+scaler)
    import org.apache.spark.SparkContext._
    val spark = SparkSession.builder.appName("RestaurantCluster").getOrCreate()
    val dataset = spark.read.format("libsvm").load(inputPath + postfix)
    var data:RDD[(Double, org.apache.spark.mllib.linalg.Vector)] = null
    if("Maxscaler".equals(scaler)){
       data  = Maxscaler(spark, dataset).sortByKey(true, 1)
    }else if("VectorIndexer".equals(scaler)){
    	data  = VectorIndexer(spark, dataset).sortByKey(true, 1)
    }else if("normalizer".equals(scaler)){
    	data  = normalizer(spark, dataset).sortByKey(true, 1)
    }else if("StandardScaler".equals(scaler)){
    	data  = StandardScaler(spark, dataset).sortByKey(true, 1)
    }else if("MinMaxScaler".equals(scaler)){
    	data  = MinMaxScaler(spark, dataset).sortByKey(true, 1)
    }else if("TFidfScaler".equals(scaler)){
    	data  = TFidfScaler(spark, dataset).sortByKey(true, 1)
    }else{
      data = dataset.rdd.map { x => x.getAs[Double](0) -> Vectors.dense(x.getAs[SparseVector](1).toDense.values) }.sortByKey(true, 1)
    }
    val vectorRdd = data.map(x => x._2);
    val clusters = KMeans.train(vectorRdd, initK, maxIterations)
    println("    clusters.clusterCenters.foreach")
    clusters.clusterCenters.foreach { println }

    // Evaluate clustering by computing Within Set Sum of Squared Errors
    val WSSSE = clusters.computeCost(vectorRdd)
    println("Within Set Sum of Squared Errors = " + WSSSE)
    data.saveAsTextFile("/user/hadoop/ufsCluster/kmeans_data_top" + postfix + "_k" + initK+"_"+scaler)
    clusters.save(spark.sparkContext, "/user/hadoop/ufsCluster/kmeans_top" + postfix + "_k" + initK+"_"+scaler)
    clusters.predict(vectorRdd).saveAsTextFile("/user/hadoop/ufsCluster/kmeans_predict_top" + postfix + "_k" + initK+"_"+scaler)
  }

  def Maxscaler(spark: SparkSession, dataFrame: org.apache.spark.sql.DataFrame): RDD[(Double, org.apache.spark.mllib.linalg.Vector)] = {
    val data = dataFrame.toDF("id", "features")
    val scaler = new MaxAbsScaler()
      .setInputCol("features")
      .setOutputCol("scaledFeatures")
    val scalerModel = scaler.fit(data)
    val scaledData = scalerModel.transform(data)
    scaledData.select("id", "scaledFeatures").rdd.map { x => x.getAs[Double](0) -> Vectors.dense(x.getAs[SparseVector](1).toDense.values) }
  }

  def VectorIndexer(spark: SparkSession, dataFrame: org.apache.spark.sql.DataFrame): RDD[(Double, org.apache.spark.mllib.linalg.Vector)] = {
    val data = dataFrame.toDF("id", "features")
    val indexer = new VectorIndexer()
      .setInputCol("features")
      .setOutputCol("scaledFeatures")
      .setMaxCategories(10);
    val indexerModel = indexer.fit(data);
    val scaledFeatures = indexerModel.transform(data)
    scaledFeatures.select("id", "scaledFeatures").rdd.map { x => x.getAs[Double](0) -> Vectors.dense(x.getAs[SparseVector](1).toDense.values) }

  }

  def normalizer(spark: SparkSession, dataFrame: org.apache.spark.sql.DataFrame): RDD[(Double, org.apache.spark.mllib.linalg.Vector)] = {
    val data = dataFrame.toDF("id", "features")
    val normalizer = new Normalizer()
      .setInputCol("features")
      .setOutputCol("scaledFeatures")
      .setP(1.0)
    val scaledFeatures = normalizer.transform(data)
    scaledFeatures.select("id", "scaledFeatures").rdd.map { x => x.getAs[Double](0) -> Vectors.dense(x.getAs[SparseVector](1).toDense.values) }

  }

  def StandardScaler(spark: SparkSession, dataFrame: org.apache.spark.sql.DataFrame): RDD[(Double, org.apache.spark.mllib.linalg.Vector)] = {
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

  def MinMaxScaler(spark: SparkSession, dataFrame: org.apache.spark.sql.DataFrame): RDD[(Double, org.apache.spark.mllib.linalg.Vector)] = {
    val data = dataFrame.toDF("id", "features")
    val scaler = new MinMaxScaler()
      .setInputCol("features")
      .setOutputCol("scaledFeatures")
    val indexerModel = scaler.fit(data);
    val scaledFeatures = indexerModel.transform(data)
    scaledFeatures.select("id", "scaledFeatures").rdd.map { x => x.getAs[Double](0) -> Vectors.dense(x.getAs[SparseVector](1).toDense.values) }

  }

  def TFidfScaler(spark: SparkSession, dataFrame: org.apache.spark.sql.DataFrame): RDD[(Double, org.apache.spark.mllib.linalg.Vector)] = {
    val data = dataFrame.toDF("id", "features")
    val idf = new IDF()
      .setInputCol("features")
      .setOutputCol("scaledFeatures")
    val indexerModel = idf.fit(data);
    val scaledFeatures = indexerModel.transform(data)
    scaledFeatures.select("id", "scaledFeatures").rdd.map { x => x.getAs[Double](0) -> Vectors.dense(x.getAs[SparseVector](1).toDense.values) }

  }

}