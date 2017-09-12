
import AssemblyKeys._

assemblySettings



name := "sparkML"


version := "1.0"

scalaVersion := "2.11.8"
javacOptions ++= Seq("-source", "1.7", "-target", "1.7")
resolvers += "Typesafe Repo" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies ++= {

  val sparkVersion = "2.1.0"
  val hadoopVersion = "2.4.0"
  val hbaseVersion = "1.2.4"
  
  Seq(
    "org.apache.spark" % "spark-core_2.11" % sparkVersion ,
    "org.apache.spark" % "spark-sql_2.11" % sparkVersion ,
    "org.apache.spark" % "spark-mllib_2.11" % sparkVersion ,
    "org.apache.spark" % "spark-streaming_2.11" % sparkVersion ,
    "org.apache.spark" % "spark-streaming-kafka_2.11" % "1.6.1",
    "org.apache.spark" % "spark-streaming-flume_2.11" % sparkVersion,
    "org.apache.spark" % "spark-streaming-flume-sink_2.11" % sparkVersion,
    "org.apache.spark" % "spark-graphx_2.11" % sparkVersion,
    "org.apache.spark" % "spark-catalyst_2.11" % sparkVersion,
    "org.apache.hadoop" % "hadoop-client" % hadoopVersion,

    //"org.apache.phoenix" % "phoenix-spark" % hbaseVersion,
    //"org.apache.phoenix" % "phoenix-core" % hbaseVersion,
    "org.apache.hbase" % "hbase-client" % hbaseVersion,
    "org.apache.hbase" % "hbase-common" % hbaseVersion,
    "org.apache.hbase" % "hbase-server" % hbaseVersion,
    "org.apache.htrace" % "htrace-core" % "3.1.0-incubating",
    "org.apache.hbase" % "hbase-protocol" % hbaseVersion,
    "org.mongodb" % "mongo-java-driver" % "3.3.0",
    "com.stratio.datasource" % "spark-mongodb_2.11" % "0.11.2",
    "com.github.scopt" % "scopt_2.11" % "3.2.0"
  )
    .map(_ % "provided")
    .map(_.excludeAll(ExclusionRule(organization = "org.mortbay.jetty")))
}

libraryDependencies ++={
  Seq(
     //"com.typesafe.play" %% "play-json" % "2.2.1",
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
    "net.sf.json-lib" % "json-lib" % "1.0" classifier "jdk15"
  ).map(
    _.excludeAll(ExclusionRule(organization = "org.mortbay.jetty"))
  )

}



resolvers += Resolver.sonatypeRepo("public")
jarName in assembly := "UFSRepice.jar"