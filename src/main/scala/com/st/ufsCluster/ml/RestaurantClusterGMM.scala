package com.ufs.cluster.ml

import org.apache.spark.sql.SparkSession
import org.apache.spark.ml.clustering.GaussianMixture
import org.apache.spark.sql.SQLContext
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

/**
 * @author socket
 */
object RestaurantClusterGMM {
  def main(args: Array[String]) {

    val conf = new SparkConf().setAppName("RestaurantCluster").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    val dataset = sqlContext.read.format("libsvm").load("/home/socket/git/workspace_scala/data/mllib/sample_kmeans_data.txt")

    // Trains Gaussian Mixture Model
    val gmm = new GaussianMixture()
      .setK(3)
    val model = gmm.fit(dataset)

    // output parameters of mixture model model
    for (i <- 0 until model.getK) {
      println("weight=%f\nmu=%s\nsigma=\n%s\n" format
        (model.weights(i), model.gaussians(i).mean, model.gaussians(i).cov))
    }
    // $example off$
    val result = model.transform(dataset)
    result.show(false)

  }

}