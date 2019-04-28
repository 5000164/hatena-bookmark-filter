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

  SourceCodeGenerator.main(
    Array(
      "slick.jdbc.H2Profile",
      "org.h2.Driver",
      url,
      "common/src/main/scala",
      "infrastructure"
    )
  )
}
