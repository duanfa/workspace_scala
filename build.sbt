
import AssemblyKeys._

assemblySettings



name := "sparkML"


version := "1.0"

scalaVersion := "2.11.8"
javacOptions ++= Seq("-source", "1.7", "-target", "1.7")
resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= {

  val sparkVersion = "2.0.1"
  val hadoopVersion = "2.4.0"
  Seq(
    "org.apache.spark" %% "spark-core" % sparkVersion ,
    "org.apache.spark" %% "spark-sql" % sparkVersion ,
    "org.apache.spark" %% "spark-mllib" % sparkVersion ,
    //"org.apache.spark" %% "spark-streaming" % sparkVersion ,
   // "org.apache.spark" % "spark-streaming-kafka_2.10" % sparkVersion,
    // "org.scala-lang" % "scala-compiler" % "2.10.6" ,
    //"org.scala-lang" % "scala-reflect" % "2.10.6" ,
    "org.apache.spark" %% "spark-graphx" % sparkVersion ,
    "org.apache.spark" %% "spark-catalyst" % sparkVersion,
    "org.apache.hadoop" % "hadoop-client" % hadoopVersion

    /* "org.apache.phoenix" % "phoenix-spark" % "4.6.0-HBase-0.98",
    "org.apache.phoenix" % "phoenix-core" % "4.6.0-HBase-0.98",
    "org.apache.hbase" % "hbase-client" % "0.98.12-hadoop2",
    "org.apache.hbase" % "hbase-common" % "0.98.12-hadoop2",
    "org.apache.hbase" % "hbase-server" % "0.98.12-hadoop2",
   "org.apache.hbase" % "hbase-protocol" % "0.98.12-hadoop2"*/
  )
    .map(_ % "provided")
    .map(_.excludeAll(ExclusionRule(organization = "org.mortbay.jetty")))
}

libraryDependencies ++={
  Seq(
    "redis.clients" % "jedis" % "2.8.1",
    "com.huaban"%"jieba-analysis"%"1.0.2",
    "com.github.nscala-time" %% "nscala-time" % "2.2.0",
    "mysql" % "mysql-connector-java" % "5.1.34"  ,
    //    "org.scalatest" % "scalatest_2.10" % "2.0" % "test",
    "com.alibaba" % "fastjson" % "1.1.21",
    "com.github.scopt" %% "scopt" % "3.3.0",
    "sqlline" % "sqlline" % "1.1.9",
    "org.jblas" % "jblas" % "1.2.4",
    
    //"net.sf.ezmorph" % "ezmorph" % "1.0.6",
    "net.sf.json-lib" % "json-lib" % "1.0" classifier "jdk15",
    "org.scalaj" % "scalaj-http_2.11" % "2.3.0"
  ).map(
    _.excludeAll(ExclusionRule(organization = "org.mortbay.jetty"))
  )

}



resolvers += Resolver.sonatypeRepo("public")
jarName in assembly := "UFSRepice.jar"