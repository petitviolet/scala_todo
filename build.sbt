name := "myapp"

version := "1.0-SNAPSHOT"

//lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

conflictWarning := ConflictWarning.disable

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache
)

play.Project.playScalaSettings
//
//libraryDependencies += "org.twitter4j" % "twitter4j-core" % "4.0.1"
//
//resolvers ++= Seq(
//  "Sonatype snapshots repository" at "https://oss.sonatype.org/content/repositories/snapshots/"
//)
//
//libraryDependencies ++= Seq(
//  "org.pac4j"   % "play-pac4j_scala" % "1.2.0",
//  "org.pac4j" % "pac4j-oauth" % "1.5.1"
//)