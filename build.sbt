import ReleaseTransformations._

ThisBuild / organization := "com.github.Saisse"
name := "env-generator"

ThisBuild / version := "0.1-SNAPSHOT"

inThisBuild(
  List(
    scalaVersion := scalaVersion.value, // 2.11.12, 2.13.8, or 3.x
    semanticdbEnabled := true, // enable SemanticDB
    semanticdbVersion := scalafixSemanticdb.revision // only required for Scala 2.x
  )
)
ThisBuild / scalafixDependencies += "com.github.liancheng" %% "organize-imports" % "0.6.0"

lazy val core = (project in file("core")).settings(
  name := "env-generator-core",
  scalaVersion := "2.12.15",
  publish / skip := true,
  githubOwner := "Saisse",
  githubRepository := "env-generator",
  githubTokenSource := TokenSource.GitConfig("github.token"),
  libraryDependencies ++= Seq(
    "org.scalatra.scalate" %% "scalate-core" % "1.9.7",
    "org.scalatest" %% "scalatest" % "3.2.11" % Test
  )
)

lazy val plugin = (project in file("sbt-plugin"))
  .settings(
    name := "env-generator-sbt-plugin",
    sbtPlugin := true,
    scalaVersion := "2.12.15",
    githubOwner := "Saisse",
    githubRepository := "env-generator",
    githubTokenSource := TokenSource.GitConfig("github.token")
  )
  .dependsOn(core)

resolvers += Resolver.githubPackages("Saisse")

githubTokenSource := TokenSource.GitConfig("github.token")

ThisBuild / githubOwner := "Saisse"
ThisBuild / githubRepository := "env-generator"

ThisBuild / versionScheme := Some("semver-spec")

ThisBuild / publishTo := githubPublishTo.value

releaseProcess := Seq[ReleaseStep](
//  checkSnapshotDependencies,              // : ReleaseStep
  inquireVersions,                        // : ReleaseStep
  runClean,                               // : ReleaseStep
  runTest,                                // : ReleaseStep
  setReleaseVersion,                      // : ReleaseStep
  commitReleaseVersion,                   // : ReleaseStep, performs the initial git checks
  tagRelease,                             // : ReleaseStep
//  releaseStepTask(publish),
  publishArtifacts,                       // : ReleaseStep, checks whether `publishTo` is properly set up
  setNextVersion,                         // : ReleaseStep
  commitNextVersion,                      // : ReleaseStep
  pushChanges                             // : ReleaseStep, also checks that an upstream branch is properly configured
)