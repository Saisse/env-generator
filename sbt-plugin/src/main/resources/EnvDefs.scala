import env.generator._

sealed abstract class Env(val id: String) extends EnvLike
object Env {
  case class Live extends Env("live")
  case class Staging extends Env("Staging")
  case class Local extends Env("local")

  val values = Seq(Live, Staging, Local)
}

val envDefs = defs(
  env(Env.Live)(

  ),
  env(Env.Staging)()
)


val envTemplates = templates(
  template()
)

(defs, envTemplates)