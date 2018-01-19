package com.ufs.cluster.mllib

import org.apache.spark.mllib.clustering.GaussianMixture
import org.apache.spark.mllib.clustering.GaussianMixtureModel
import org.apache.spark.sql.SparkSession
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.ml.linalg.SparseVector

/**
 * @author socket
 */
object RestaurantClusterGMM {
  def main(args: Array[String]) {

	  var inputPath = "/user/hadoop/ufsCluster/ufs.libsvm100"
			  if (args.size > 0) {
				  inputPath = args(0)
			  }
   val spark = SparkSession.builder.appName("RestaurantCluster").getOrCreate()
    val sc = spark.sparkContext

    val dataset = spark.read.format("libsvm").load(inputPath)
   val  data = dataset.rdd.map { x => x.getAs[Double](0) -> Vectors.dense(x.getAs[SparseVector](1).toDense.values) }.sortByKey(true, 1)

 val vectorRdd = data.map(x => x._2);
    // $example on$
    // Load and parse the data

    // Cluster the data into two classes using GaussianMixture
    val gmm = new GaussianMixture().setK(12).run(vectorRdd)

    // Save and load model
    gmm.save(sc, "target/org/apache/spark/GaussianMixtureExample/GaussianMixtureModel")
    val sameModel = GaussianMixtureModel.load(sc,
      "target/org/apache/spark/GaussianMixtureExample/GaussianMixtureModel")

    // output parameters of max-likelihood model
    for (i <- 0 until gmm.k) {
      println("weight=%f\nmu=%s\nsigma=\n%s\n" format
        (gmm.weights(i), gmm.gaussians(i).mu, gmm.gaussians(i).sigma))
    }
    // $example off$
    
    gmm.predict(vectorRdd).saveAsTextFile("/user/hadoop/ufsCluster/gmm_predict")

    sc.stop()
  

  }

}