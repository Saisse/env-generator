package env.generator.core

import env.generator._
import org.scalatest.funspec.AnyFunSpec

import java.io.File

abstract class MockEnv(val id: String) extends EnvLike
object MockEnv {
  case object EnvA extends MockEnv("envA")
  case object EnvB extends MockEnv("envB")

  val values: Seq[MockEnv] = Seq(EnvA, EnvB)
}

class EnvGeneratorSpec extends AnyFunSpec {

  it("generate") {

    val commonDefs = commonDef(
     "commonKey" -> "common value"
      , "nullValue" -> null
      , "noneValue" -> None
    )
    println(s"commonDefs: $commonDefs")

    val envDefs = defs(
      env(MockEnv.EnvA)(
        "top" -> "top value A",
        "tree.leaf" -> "Leaf A",
        "intBalue" -> 25,
        "stringValue" -> "string A",
        "booleanBalue" -> "true"
      ),
      env(MockEnv.EnvB)(
        "top" -> "top value B",
        "tree.leaf" -> "Leaf B",
        "intBalue" -> 12,
        "stringValue" -> "string B",
        "booleanBalue" -> "false"
      )
    )

    val baseDir = new File("core/src/test/resources/env")

    val envTemplates = templates(
      template(
        "template/app-template.mustache",
        (env: MockEnv) => s"defs/${env.id}/app.conf"
      )(MockEnv.values: _*)
    )

    for {
      t <- envTemplates
      e <- t.envs
    } yield {
      EnvGenerator.generate(baseDir, commonDefs, envDefs, t, e)
    }
  }

}
