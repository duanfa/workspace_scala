package com.ufs.cluster.localTest

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext

/**
 * @author socket
 */
object test {
  val recipes = "豆,鹅,河海鲜,鸡,菌菇参,米面,牛,其他主料,肉,蔬果,蛙,虾,蟹,鸭,羊,鱼,猪,扒,拌,煲,爆,煸,炒,打,炖,风干,罐,锅,裹,烩,煎,浸,焗,烤,捞,烙,酪,熘,焖,磨,酿,泡,烹,炝,沁,膳,烧,涮,烫,煨,熏,腌,炸,蒸,炙,煮,灼,渍,冰淇淋,茶,腐乳,花果木,姜蒜葱,酱,酒,咖喱叻沙,卤,麻辣椒盐芥,奶,其他辅料,松露,糖/醋/酸,油,汁,芝孜,板,棒,钵,串,鼎,缸,糕,疙瘩,羹,盒,壶,糊,滑,浆,筋,筐,篮,粒,凉,笼,沫,泥,盆,皮,片,球,生,汤,筒,稀,其他,无关"
  def main(args: Array[String]) {
    println("useage: args(0): inputPath  args(0):recipTopN")
    val conf = new SparkConf().setAppName("RestaurantCluster").setMaster("local[2]")
    val sc = new SparkContext(conf)
    val recipesArrayBc = sc.broadcast[Array[String]](recipes.split(","))
  }
}