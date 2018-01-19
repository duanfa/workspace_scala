package com.ufs.cluster.ml

import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.clustering.BisectingKMeans

/**
 * @author socket
 */
object RestaurantClusterBisectingKmeans {
  def main(args: Array[String]) {
    // Creates a SparkSession
    val spark = SparkSession
      .builder
      .appName("BisectingKMeansExample")
      .getOrCreate()

    // $example on$
    // Loads data.
    val dataset = spark.read.format("libsvm").load("/user/hadoop/ufsCluster/ufs.libsvm")

    // Trains a bisecting k-means model.
    val bkm = new BisectingKMeans().setK(10).setSeed(1)
    val model = bkm.fit(dataset)

    // Evaluate clustering.
    val cost = model.computeCost(dataset)
    println(s"Within Set Sum of Squared Errors = $cost")

    // Shows the result.
    println("Cluster Centers: ")
    val centers = model.clusterCenters
    centers.foreach(println)
    // $example off$

    spark.stop()
  }

}