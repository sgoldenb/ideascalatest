name := "IdeaTest"

version := "0.1"

scalaVersion := "2.12.6"


libraryDependencies ++= Seq(
 "org.scalikejdbc" %% "scalikejdbc"       % "3.3.0",
 "com.h2database"  %  "h2"                % "1.4.197",
 "ch.qos.logback"  %  "logback-classic"   % "1.2.3"
)
