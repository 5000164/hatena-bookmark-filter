package tasks

import org.flywaydb.core.Flyway
import slick.codegen.SourceCodeGenerator

/** マイグレートを実行する。 */
object Migrate extends App {
  val url = "jdbc:sqlite:./db.db"

  val flyway = new Flyway()
  flyway.setDataSource(url, "", "")
  flyway.setLocations(s"filesystem:${System.getProperty("user.dir")}/migration/src/main/resources/db/migration")
  flyway.migrate()

  SourceCodeGenerator.run(
    profile = "slick.jdbc.SQLiteProfile",
    jdbcDriver = "org.sqlite.JDBC",
    url = url,
    outputDir = "collector/src/main/scala",
    pkg = "infrastructure",
    user = None,
    password = None,
    ignoreInvalidDefaults = true
  )
}
