/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// scalastyle:off println
package com.st.decomposition.PCA

// $example on$
import org.apache.spark.ml.feature.PCA
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkConf

object PCAExample {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("PCAExample").setMaster("local[2]")
    val spark = SparkSession
      .builder.config(conf)
      .getOrCreate()

    // $example on$
    val data = Array(
      Vectors.sparse(5, Seq((1, 1.0), (3, 7.0))),
      Vectors.dense(2.0, 0.0, 3.0, 4.0, 5.0),
      Vectors.dense(4.0, 0.0, 0.0, 6.0, 7.0))
      val data2 = Array(
    		  Vectors.dense(-1, -2),
    		  Vectors.dense(-1, 0),
    		  Vectors.dense(0, 0),
    		  Vectors.dense(2, 1),
    		  Vectors.dense(0, 1))
    val df = spark.createDataFrame(data2.map(Tuple1.apply)).toDF("features")

    val pca = new PCA()
      .setInputCol("features")
      .setOutputCol("pcaFeatures")
      .setK(1)
      .fit(df)

    df.show(false)
    val result = pca.transform(df).select("pcaFeatures")
    result.show(false)
    // $example off$

    spark.stop()
  }
}
// scalastyle:on println
