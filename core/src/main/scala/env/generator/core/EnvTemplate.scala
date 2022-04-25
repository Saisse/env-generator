package env.generator.core

case class EnvTemplate[E <: EnvLike](
    path: String,
    pathSchema: E => String,
    envs: E*
)
