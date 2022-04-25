package env

import env.generator.core.{EnvLike => _, _}

package object generator {

  type EnvLike = core.EnvLike

  def templates[E <: EnvLike](templates: EnvTemplate[E]*): Seq[EnvTemplate[E]] =
    templates.toSeq
  def template[E <: EnvLike](path: String, schemaPath: E => String)(
      envs: E*
  ): EnvTemplate[E] = {
    EnvTemplate(path, schemaPath, envs: _*)
  }

  def commonDef(values: (String, Any)*): Map[String, Any] = values.toMap
  def defs[E <: EnvLike](defs: EnvDef[E]*): Seq[EnvDef[E]] = defs.toSeq
  def env[E <: EnvLike](e: E)(values: (String, Any)*): EnvDef[E] =
    EnvDef[E](e, Map.empty, values.toMap)
}
