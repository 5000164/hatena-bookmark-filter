package infrastructure
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.H2Profile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Articles.schema ++ FlywaySchemaHistory.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Articles
   *  @param id Database column ID SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param url Database column URL SqlType(VARCHAR)
   *  @param settingsId Database column SETTINGS_ID SqlType(TINYINT)
   *  @param posted Database column POSTED SqlType(BOOLEAN)
   *  @param createdAt Database column CREATED_AT SqlType(TIMESTAMP)
   *  @param updatedAt Database column UPDATED_AT SqlType(TIMESTAMP) */
  case class ArticlesRow(id: Long, url: String, settingsId: Byte, posted: Boolean, createdAt: java.sql.Timestamp, updatedAt: java.sql.Timestamp)
  /** GetResult implicit for fetching ArticlesRow objects using plain SQL queries */
  implicit def GetResultArticlesRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Byte], e3: GR[Boolean], e4: GR[java.sql.Timestamp]): GR[ArticlesRow] = GR{
    prs => import prs._
    ArticlesRow.tupled((<<[Long], <<[String], <<[Byte], <<[Boolean], <<[java.sql.Timestamp], <<[java.sql.Timestamp]))
  }
  /** Table description of table ARTICLES. Objects of this class serve as prototypes for rows in queries. */
  class Articles(_tableTag: Tag) extends profile.api.Table[ArticlesRow](_tableTag, "ARTICLES") {
    def * = (id, url, settingsId, posted, createdAt, updatedAt) <> (ArticlesRow.tupled, ArticlesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(url), Rep.Some(settingsId), Rep.Some(posted), Rep.Some(createdAt), Rep.Some(updatedAt)).shaped.<>({r=>import r._; _1.map(_=> ArticlesRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ID SqlType(BIGINT), AutoInc, PrimaryKey */
    val id: Rep[Long] = column[Long]("ID", O.AutoInc, O.PrimaryKey)
    /** Database column URL SqlType(VARCHAR) */
    val url: Rep[String] = column[String]("URL")
    /** Database column SETTINGS_ID SqlType(TINYINT) */
    val settingsId: Rep[Byte] = column[Byte]("SETTINGS_ID")
    /** Database column POSTED SqlType(BOOLEAN) */
    val posted: Rep[Boolean] = column[Boolean]("POSTED")
    /** Database column CREATED_AT SqlType(TIMESTAMP) */
    val createdAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("CREATED_AT")
    /** Database column UPDATED_AT SqlType(TIMESTAMP) */
    val updatedAt: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("UPDATED_AT")

    /** Uniqueness Index over (url,settingsId) (database name CONSTRAINT_INDEX_E) */
    val index1 = index("CONSTRAINT_INDEX_E", (url, settingsId), unique=true)
  }
  /** Collection-like TableQuery object for table Articles */
  lazy val Articles = new TableQuery(tag => new Articles(tag))

  /** Entity class storing rows of table FlywaySchemaHistory
   *  @param installedRank Database column installed_rank SqlType(INTEGER), PrimaryKey
   *  @param version Database column version SqlType(VARCHAR), Length(50,true)
   *  @param description Database column description SqlType(VARCHAR), Length(200,true)
   *  @param `type` Database column type SqlType(VARCHAR), Length(20,true)
   *  @param script Database column script SqlType(VARCHAR), Length(1000,true)
   *  @param checksum Database column checksum SqlType(INTEGER)
   *  @param installedBy Database column installed_by SqlType(VARCHAR), Length(100,true)
   *  @param installedOn Database column installed_on SqlType(TIMESTAMP)
   *  @param executionTime Database column execution_time SqlType(INTEGER)
   *  @param success Database column success SqlType(BOOLEAN) */
  case class FlywaySchemaHistoryRow(installedRank: Int, version: Option[String], description: String, `type`: String, script: String, checksum: Option[Int], installedBy: String, installedOn: java.sql.Timestamp, executionTime: Int, success: Boolean)
  /** GetResult implicit for fetching FlywaySchemaHistoryRow objects using plain SQL queries */
  implicit def GetResultFlywaySchemaHistoryRow(implicit e0: GR[Int], e1: GR[Option[String]], e2: GR[String], e3: GR[Option[Int]], e4: GR[java.sql.Timestamp], e5: GR[Boolean]): GR[FlywaySchemaHistoryRow] = GR{
    prs => import prs._
    FlywaySchemaHistoryRow.tupled((<<[Int], <<?[String], <<[String], <<[String], <<[String], <<?[Int], <<[String], <<[java.sql.Timestamp], <<[Int], <<[Boolean]))
  }
  /** Table description of table flyway_schema_history. Objects of this class serve as prototypes for rows in queries.
   *  NOTE: The following names collided with Scala keywords and were escaped: type */
  class FlywaySchemaHistory(_tableTag: Tag) extends profile.api.Table[FlywaySchemaHistoryRow](_tableTag, "flyway_schema_history") {
    def * = (installedRank, version, description, `type`, script, checksum, installedBy, installedOn, executionTime, success) <> (FlywaySchemaHistoryRow.tupled, FlywaySchemaHistoryRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(installedRank), version, Rep.Some(description), Rep.Some(`type`), Rep.Some(script), checksum, Rep.Some(installedBy), Rep.Some(installedOn), Rep.Some(executionTime), Rep.Some(success)).shaped.<>({r=>import r._; _1.map(_=> FlywaySchemaHistoryRow.tupled((_1.get, _2, _3.get, _4.get, _5.get, _6, _7.get, _8.get, _9.get, _10.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column installed_rank SqlType(INTEGER), PrimaryKey */
    val installedRank: Rep[Int] = column[Int]("installed_rank", O.PrimaryKey)
    /** Database column version SqlType(VARCHAR), Length(50,true) */
    val version: Rep[Option[String]] = column[Option[String]]("version", O.Length(50,varying=true))
    /** Database column description SqlType(VARCHAR), Length(200,true) */
    val description: Rep[String] = column[String]("description", O.Length(200,varying=true))
    /** Database column type SqlType(VARCHAR), Length(20,true)
     *  NOTE: The name was escaped because it collided with a Scala keyword. */
    val `type`: Rep[String] = column[String]("type", O.Length(20,varying=true))
    /** Database column script SqlType(VARCHAR), Length(1000,true) */
    val script: Rep[String] = column[String]("script", O.Length(1000,varying=true))
    /** Database column checksum SqlType(INTEGER) */
    val checksum: Rep[Option[Int]] = column[Option[Int]]("checksum")
    /** Database column installed_by SqlType(VARCHAR), Length(100,true) */
    val installedBy: Rep[String] = column[String]("installed_by", O.Length(100,varying=true))
    /** Database column installed_on SqlType(TIMESTAMP) */
    val installedOn: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("installed_on")
    /** Database column execution_time SqlType(INTEGER) */
    val executionTime: Rep[Int] = column[Int]("execution_time")
    /** Database column success SqlType(BOOLEAN) */
    val success: Rep[Boolean] = column[Boolean]("success")

    /** Index over (success) (database name flyway_schema_history_s_idx) */
    val index1 = index("flyway_schema_history_s_idx", success)
  }
  /** Collection-like TableQuery object for table FlywaySchemaHistory */
  lazy val FlywaySchemaHistory = new TableQuery(tag => new FlywaySchemaHistory(tag))
}
