package tasks

import org.flywaydb.core.Flyway

/** マイグレートを実行する。 */
object Migrate extends App {
  val flyway = new Flyway()
  flyway.setDataSource("jdbc:sqlite:./db.db", "", "")
  flyway.setLocations(s"filesystem:${System.getProperty("user.dir")}/src/main/resources/db/migration")
  flyway.migrate()
}
