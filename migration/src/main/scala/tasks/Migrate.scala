package tasks

import org.flywaydb.core.Flyway
import slick.codegen.SourceCodeGenerator

/** マイグレートを実行する。 */
object Migrate extends App {
  val url = "jdbc:h2:./db"

  val flyway = new Flyway()
  flyway.setDataSource(url, "", "")
  flyway.setLocations(s"filesystem:${System.getProperty("user.dir")}/migration/src/main/resources/db/migration")
  flyway.migrate()

  SourceCodeGenerator.run(
    profile = "slick.jdbc.H2Profile",
    jdbcDriver = "org.h2.Driver",
    url = url,
    outputDir = "common/src/main/scala",
    pkg = "infrastructure",
    user = None,
    password = None,
    ignoreInvalidDefaults = true
  )
}
