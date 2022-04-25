package env.generator.plugin

import env.generator.core.{EnvDef, EnvGenerator, EnvLike, EnvTemplate}
import sbt.*
import sbt.Keys.*

import java.io.File
import scala.io.Source
import scala.reflect.runtime.*
import scala.tools.reflect.ToolBox

object EnvGeneratorPlugin extends AutoPlugin {
  override def requires: Plugins = empty
  override def trigger = allRequirements

  val logger = sbt.internal.util.ConsoleLogger(System.out)

  object autoImport {
    val envDirectory = settingKey[File]("base env directory")
    val envDefsFile = settingKey[File]("env defs file")
    val envTemplatesFile = settingKey[File]("env templates file")
    val envGenerate = TaskKey[Unit]("envGenerate")
  }

  import autoImport.*

  override lazy val projectSettings: Seq[Setting[_]] = Seq(
    envDirectory := baseDirectory.value / "env",
    envDefsFile := envDirectory.value / "EnvDefs.scala",
    envGenerate := {
      println(s"generate ${envDirectory.value}")

      type E = EnvLike
      val (common, defs, templates) =
        evalFile[(Map[String, Any], Seq[EnvDef[E]], Seq[EnvTemplate[E]])](
          envDefsFile.value
        )

      templates.foreach { t =>
        val path = envDirectory.value / t.path
        if (!path.exists()) {
          sys.error(s"template file $path is not found")
        }
        t.envs.foreach { e =>
          if (!defs.exists(_.env == e)) {
            sys.error(s"not found def for $e in defs")
          }
        }
      }

      for {
        t <- templates
        e <- t.envs
      } yield {
        logger.info(s"generate ${t.pathSchema(e)}")
        EnvGenerator.generate(
          envDirectory.value,
          common,
          defs,
          t,
          e
        )
      }

    }
  )

  private def eval[A](string: String): A = {
    val toolbox = currentMirror.mkToolBox()
    val tree = toolbox.parse(string)
    toolbox.eval(tree).asInstanceOf[A]
  }

  private def evalFile[A](file: File): A =
    eval(Source.fromFile(file).mkString(""))
}
