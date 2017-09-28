package com.st.test


import org.apache.spark.ml.feature.{HashingTF, IDF, Tokenizer}
import org.apache.spark.sql.SparkSession

object IDFTest {

	 def main(args: Array[String]) {
      val spark = SparkSession.builder.appName("RestaurantCluster").master("local[2]").getOrCreate()
		 val sentenceData = spark.createDataFrame(Seq(
				  (0.0, "Hi I heard about Spark"),
				  (0.0, "I wish Java could use case classes"),
				  (1.0, "Logistic regression models are neat")
				)).toDF("label", "sentence")

				val tokenizer = new Tokenizer().setInputCol("sentence").setOutputCol("words")
				val wordsData = tokenizer.transform(sentenceData)
        
        println("----------wordsData-----------")
        wordsData.show(false)

				val hashingTF = new HashingTF()
				  .setInputCol("words").setOutputCol("rawFeatures").setNumFeatures(20)

				val featurizedData = hashingTF.transform(wordsData)
				// alternatively, CountVectorizer can also be used to get term frequency vectors

				println("----------featurizedData-----------")
        featurizedData.printSchema()
        featurizedData.show(false)
				val idf = new IDF().setInputCol("rawFeatures").setOutputCol("features")
				val idfModel = idf.fit(featurizedData)

				val rescaledData = idfModel.transform(featurizedData)
				println("----------rescaledData-----------")
				rescaledData.printSchema()
				rescaledData.select("label", "features").show(false)
	 }
	
}

