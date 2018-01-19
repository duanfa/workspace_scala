package com.ufs.cluster.ml

import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.clustering.KMeans
import org.apache.spark.sql.SQLContext
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.ml.linalg.Vectors

/**
 * @author socket
 */
object RestaurantClusterKmeans {
  def main(args: Array[String]) {
    //    val spark = SparkSession.builder.appName("RestaurantCluster").getOrCreate()
    //    spark.conf.set("Master","local[2]")
    /*    val dataset = spark.read.format("libsvm").load("/user/hadoop/ufsCluster/ufs.libsvm")
    
     // Trains a k-means model.
    val kmeans = new KMeans().setK(10).setSeed(1L)
    val model = kmeans.fit(dataset)

    // Evaluate clustering by computing Within Set Sum of Squared Errors.
    val WSSSE = model.computeCost(dataset)
    println(s"Within Set Sum of Squared Errors = $WSSSE")

    // Shows the result.
    println("Cluster Centers: ")
    model.clusterCenters.foreach(println)*/
    // $example off$
    val conf = new SparkConf().setAppName("RestaurantCluster").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    val dataset: org.apache.spark.sql.DataFrame = sqlContext.createDataFrame(Seq(
      (1, Vectors.dense(0.0, 0.0, 0.0)),
      (2, Vectors.dense(0.1, 0.1, 0.1)),
      (3, Vectors.dense(0.2, 0.2, 0.2)),
      (4, Vectors.dense(9.0, 9.0, 9.0)),
      (5, Vectors.dense(9.1, 9.1, 9.1)),
      (6, Vectors.dense(9.2, 9.2, 9.2)))).toDF("id", "features")

    // Trains a k-means model
    val kmeans = new KMeans()
      .setK(2)
      .setFeaturesCol("features")
      .setPredictionCol("prediction")
    val model = kmeans.fit(dataset)
    val WSSSE = model.computeCost(dataset)
    println(s"Within Set Sum of Squared Errors = $WSSSE")
    val result = model.transform(dataset)
    result.printSchema()
result.select("id", "features", "prediction").show(false)
    // Shows the result
    println("Final Centers: ")
    model.clusterCenters.foreach(println)
    //    val centers = model.
    //    spark.stop()

  }

}