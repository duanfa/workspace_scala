package com.ufs.cluster.localTest

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import scala.io.Source

/**
 * @author socket
 */
object test {
  def main(args: Array[String]) {
    val filePath ="/home/socket/Desktop/site/sku.txt"
    val file= Source.fromFile(filePath)
    for(line <- file.getLines)
    {
       sku(line)
    }
    file.close
  }
  def sku (content: String) = {
    var b = false
    if (content.contains("克")) {
      b = true
    } else if(content.contains("酱")){
    	b = true
    } else if(content.contains("汁")){
    	b = true
    } else if(content.contains("精")){
    	b = true
    } else if(content.contains("html")){
      b = false
    }
    if(!b){
      println(content)
    }
  }
}