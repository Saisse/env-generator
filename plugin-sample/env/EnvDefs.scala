import env.generator._

sealed class Env(val id: String) extends EnvLike
object Env {
  case object Develop extends Env("develop")
  case object Staging extends Env("staging")

  val values = Seq(Develop, Staging)
}

val common = commonDef()

val envDefs = defs(
  env(Env.Develop)(
    "app.key" -> "Dev"
  ),
  env(Env.Staging)(
    "app.key" -> "Staging"
  )
)

val envTemplates = templates(
  template(
    "templates/app-template.mustache",
    (e: Env) => s"defs/${e.id}/app.conf"
  )(Env.values:_*)

)
(common, envDefs, envTemplates)