package com.ufs.cluster.mllib

import org.apache.spark.sql.SparkSession
import org.apache.spark.mllib.clustering.BisectingKMeans
import org.apache.spark.ml.linalg.SparseVector
import org.apache.spark.mllib.linalg.Vectors
/**
 * @author socket
 */
object RestaurantClusterBisectingKmeans {
  def main(args: Array[String]) {

	  var initK = 6
    var maxIterations = 100
    var inputPath = "/user/hadoop/ufsCluster/ufs.libsvm"
    var postfix = ""
    println("useage: args(0): initK  args(1):maxIterations args(2):inputPath args(3):postfix")
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
    println("params: initK:" + initK + " maxIterations:" + maxIterations +" inputPath:"+inputPath+postfix +" postfix:"+postfix)
    val spark = SparkSession.builder.appName("RestaurantCluster").getOrCreate()
    val sc = spark.sparkContext
    val dataset = spark.read.format("libsvm").load(inputPath+postfix)
    val data = dataset.rdd.map { x => Vectors.dense(x.getAs[SparseVector](1).toDense.values) }
    val bkm = new BisectingKMeans().setK(initK).setMaxIterations(maxIterations)
    val model = bkm.run(data)

    model.clusterCenters.zipWithIndex.foreach {
      case (center, idx) =>
        println(s"Cluster Center ${idx}: ${center}")
    }
    // $example off$、
    val WSSSE = model.computeCost(data)
    println("Within Set Sum of Squared Errors = " + WSSSE)
     model.save(spark.sparkContext, "/user/hadoop/ufsCluster/bisectingkmeans_model_top"+postfix+"_k"+initK)
    model.predict(data).saveAsTextFile("/user/hadoop/ufsCluster/bisectingkmeans_predict_top"+postfix+"_k"+initK)

    sc.stop()
  }

}