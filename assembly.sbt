import AssemblyKeys._

assemblySettings

mergeStrategy in assembly := {
  case PathList(ps @ _*) if ps.last endsWith "pom.properties" =>
    MergeStrategy.discard
  case PathList(ps @ _*) if ps.last endsWith "pom.xml" =>
    MergeStrategy.discard
  case PathList("com", "esotericsoftware","minlog", xs @ _*)         => MergeStrategy.first
  case PathList("com", "google","common", xs @ _*)         => MergeStrategy.first
  case PathList("org", "apache","commons", xs @ _*)         => MergeStrategy.first
  case PathList("org", "apache","spark", xs @ _*)         => MergeStrategy.first
  case PathList("org", "apache","jasper", xs @ _*)         => MergeStrategy.first
  case PathList("edu", "umd","cs", xs @ _*)         => MergeStrategy.first
  case PathList("javax", "servlet", xs @ _*)         => MergeStrategy.first
  case PathList("com", "esotericsoftware", "minlog","Log","Logger.class") => MergeStrategy.discard
  case PathList(ps @ _*) if ps.last endsWith "Log$Logger.class" => MergeStrategy.discard
  case PathList(ps @ _*) if ps.last endsWith "Function.class" => MergeStrategy.first
    case PathList(ps @ _*) if ps.last endsWith "Absent.class" => MergeStrategy.first
    case PathList(ps @ _*) if ps.last endsWith "package-info.class" => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith "OverrideMustInvoke.class" => MergeStrategy.first
    case PathList(ps @ _*) if ps.last endsWith "Optional$1$1.class" => MergeStrategy.first
  case x =>
    val oldStrategy = (mergeStrategy in assembly).value
    oldStrategy(x)
}
