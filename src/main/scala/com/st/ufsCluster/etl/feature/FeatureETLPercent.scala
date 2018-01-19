package com.st.ufsCluster.etl.feature

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.sql.SQLContext
import org.apache.spark.rdd.RDD.rddToOrderedRDDFunctions
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions
/**
 * @author socket
 */
object FeatureETLPercent {
  val features = "豆,鹅,河海鲜,鸡,菌菇参,米面,牛,其他主料,肉,蔬果,蛙,虾,蟹,鸭,羊,鱼,猪,扒,拌,煲,爆,煸,炒,冻,炖,风干,罐,锅,烩,煎,浸,焗,烤,扣,捞,烙,酪,晾,熘,冒,焖,磨,酿,泡,烹,炝,沁,膳,烧,涮,烫,煨,熏,腌,炸,蒸,炙,煮,灼,渍,冰淇淋,茶,腐乳,花果木,姜蒜葱,酱,酒,咖喱叻沙,卤,麻辣椒盐芥,奶,其他辅料,松露,松仁,糖醋酸,五十三十八香,油,汁,芝孜吉士,板,棒,杯,冰,钵,串,带,丁,鼎,堆,干,缸,糕,疙瘩,羹,盒,壶,糊,滑,夹,浆,筋,卷,块,筐,篮,粒,凉,笼,沫,囊,泥,盆,皮,片,球,圈,仁,生,丝,挞,坛,汤,条,筒,稀,咸,掌"

  def main(args: Array[String]) {
    println("useage: args(0): inputPath  args(0):recipTopN")
    val conf = new SparkConf().setAppName("RestaurantCluster")
    val sc = new SparkContext(conf)
    val recipesArrayBc = sc.broadcast[Array[String]](features.split(","))
    var featurSize = recipesArrayBc.value.size
    var inputPath = "/user/hadoop/ufsClusterInput/feature1228.csv"
    if (args.size > 0) {
      inputPath = args(0)
    }

    println("params: inputPath:" + inputPath +" featurSize:"+featurSize)
    val sqlContext = new SQLContext(sc)
    val allResult = sc.textFile(inputPath).map { line => splitLine(line, recipesArrayBc) }.reduceByKey(_ ++ _).mapValues(list => {
      val recipeNums = scala.collection.mutable.Map.empty[String, Int]
      list.map(recipeNum => {
        val oldNum = recipeNums.get(recipeNum._1)
        if (oldNum != None) {
          recipeNums.put(recipeNum._1, oldNum.get + recipeNum._2)
        } else {
          recipeNums.put(recipeNum._1, recipeNum._2)
        }
      })
      recipeNums
    })
    allResult.cache()
    import org.apache.spark.SparkContext._

    val sample = allResult
    val featureMapAll = sample.flatMap { x =>
      {
        x._2.toSeq
      }
    }.aggregate(scala.collection.mutable.Map.empty[String, Int])(seqOp, combop).seq
val array = featureMapAll.toArray
   val featureTop = sc.parallelize(array).map(f=>{f._2->f._1}).sortByKey(false).take(featurSize)
    val top_recipe_orderMap = sc.parallelize(featureTop).map(f => f._2).zipWithIndex().collect().toMap
    //    val restaurantMap = sample.map { x => x._1 }.aggregate(scala.collection.mutable.Set.empty[String])(_ + _, _ ++ _).zipWithIndex.toMap
    //    val restaurantMapBC = sc.broadcast(restaurantMap)
    val featureMapBC = sc.broadcast(top_recipe_orderMap)
    val formatResult = sample.map { record =>
      {
        var sum = 0.0
        record._2.map(x => {
          if (x._2 > 0) {
            val recipeId = featureMapBC.value.get(x._1);
            if (recipeId != None) {
              sum = sum + x._2
            }
          }
        })

        val array = Array.fill(featurSize)(0.0)
        record._2.map(x => {
          if (x._2 > 0) {
            val recipeId = featureMapBC.value.get(x._1);
            if (recipeId != None) {
              if (sum > 0) {
                array(recipeId.get.toInt) = x._2 / sum
              } else {
                array(recipeId.get.toInt) = 0
              }
            }
          }
        })

        var result = record._1 + ""
        val size = featurSize - 1
        for (i <- 0 to size) {
          result = result + " " + (i + 1) + ":" + array(i.toInt)
        }
        result
      }
    }
    sc.parallelize(featureMapAll.toSeq).saveAsTextFile("/user/hadoop/ufsCluster/Dir_recipeMapAll_feature_percent")
    sc.parallelize(featureTop).saveAsTextFile("/user/hadoop/ufsCluster/Dir_recipeTop_feature_percent")
    sc.parallelize(featureMapBC.value.toSeq).saveAsTextFile("/user/hadoop/ufsCluster/Dir_recipeMapTop_feature_percent")
    formatResult.saveAsTextFile("/user/hadoop/ufsCluster/Dir_formatResult_feature_percent")
  }

  def splitLine(line: String, recipesArrayBc: Broadcast[Array[String]]): (String, scala.collection.mutable.ListBuffer[(String, Int)]) = {
    val recipeNums = scala.collection.mutable.ListBuffer.empty[(String, Int)]
    val countArray = line.split(",", 4204);
    var i = 0;
    val lenth = recipesArrayBc.value.size
    while (i < lenth) {
      recipeNums.+=((recipesArrayBc.value(i), countArray(i + 4).toInt))
      i = i + 1
    }
    countArray(0) -> recipeNums
  }
  
  def seqOp(U: scala.collection.mutable.Map[String, Int], T: (String, Int)): scala.collection.mutable.Map[String, Int] = {

    val oldNum = U.get(T._1)
    if (oldNum != None) {
      U.put(T._1, oldNum.get + T._2)
    } else {
      U.put(T._1, T._2)
    }
    U
  }
  def combop(U1: scala.collection.mutable.Map[String, Int], U2: scala.collection.mutable.Map[String, Int]): scala.collection.mutable.Map[String, Int] = {
    U2.seq.map(T => {
      val oldNum = U1.get(T._1)
      if (oldNum != None) {
        U1.put(T._1, oldNum.get + T._2)
      } else {
        U1.put(T._1, T._2)
      }
    })
    U1
  }

}