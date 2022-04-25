package env.generator.core

case class EnvDef[+E <: EnvLike](
    env: E,
    common: Map[String, Any],
    values: Map[String, Any]
)
