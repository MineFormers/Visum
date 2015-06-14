resolvers += Resolver.sonatypeRepo("releases")

import sbt.Keys._

lazy val root = project in file(".") settings(
  name := "visum",
  version := "1.0",
  scalaVersion := "2.11.6",
  autoCompilerPlugins := true,
  scalacOptions += "-optimise",
  scalacOptions += "-Yclosure-elim",
  scalacOptions += "-Yinline",
  scalacOptions += "-Ybackend:GenBCode",
  scalacOptions += "-Xexperimental",
  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0-M5" cross CrossVersion.full),
  scalacOptions += "-Xplugin-require:macroparadise",
  libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test") dependsOn macros
lazy val macros = project in file("macros") settings(
  name := "visum-macros",
  version := "1.0",
  scalaVersion := "2.11.6",
  autoCompilerPlugins := true,
  scalacOptions += "-optimise",
  scalacOptions += "-Yclosure-elim",
  scalacOptions += "-Yinline",
  scalacOptions += "-Ybackend:GenBCode",
  scalacOptions += "-Xexperimental",
  addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0-M5" cross CrossVersion.full),
  scalacOptions += "-Xplugin-require:macroparadise",
  libraryDependencies += "org.scala-lang" % "scala-reflect" % scalaVersion.value
  )