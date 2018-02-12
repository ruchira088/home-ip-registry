package com.ruchij.dao

import java.sql.Timestamp

import com.ruchij.exceptions.EmptyDatabaseException
import com.ruchij.models.Ping
import org.joda.time.DateTime
import slick.ast.BaseTypedType
import slick.jdbc.{JdbcType, SQLiteProfile}
import slick.jdbc.SQLiteProfile._
import slick.jdbc.meta.MTable
import slick.lifted._

import scala.concurrent.{ExecutionContext, Future}

class SlickPingDao(db: SQLiteProfile.backend.Database)
                  (implicit executionContext: ExecutionContext) extends PingDao
{
  import SlickPingDao._
  import api._

  private class PingTable(tag: Tag) extends Table[Ping](tag, TABLE_NAME)
  {
    implicit def dateColumn: JdbcType[DateTime] with BaseTypedType[DateTime] =
      MappedColumnType.base[DateTime, Timestamp] (
        dateTime => new Timestamp(dateTime.getMillis),
        timeStamp => new DateTime(timeStamp.getTime)
      )

    def id = column[String]("id", O.PrimaryKey)
    def timeStamp = column[DateTime]("timestamp")
    def ip = column[String]("ip")

    override def * : ProvenShape[Ping]  =
      (id, timeStamp, ip) <> ((Ping.apply _).tupled, Ping.unapply)
  }

  val pings = TableQuery[PingTable]

  override def insert(ping: Ping): Future[Ping] =
    db.run(pings += ping).map(_ => ping)

  override def getLatestPing(): Future[Ping] =
    db.run(pings.sortBy(_.timeStamp).result)
        .flatMap {
          case pingEntries if pingEntries.nonEmpty => Future.successful(pingEntries.head)
          case _ => Future.failed(EmptyDatabaseException)
        }

  def createTableIfNonExistent(): Future[Boolean] =
    for {
      tables <- db.run(MTable.getTables(TABLE_NAME))
      tableExists = tables.exists(_.name.name == TABLE_NAME)
      _ <- if (tableExists) Future.successful() else db.run(pings.schema.create)
    }
    yield !tableExists
}

object SlickPingDao
{
  val TABLE_NAME = "ping-table"
}
