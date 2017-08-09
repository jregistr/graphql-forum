package repositories

import akka.actor.ActorSystem
import akka.dispatch.MessageDispatcher
import play.api.db.slick.DatabaseConfigProvider
import slick.basic.DatabaseConfig
import slick.jdbc.PostgresProfile

abstract class Repository(system: ActorSystem, dbConfigProvider: DatabaseConfigProvider) {

  protected implicit val context: MessageDispatcher = system.dispatchers.lookup("db-dispatcher")
  protected val dbConfig: DatabaseConfig[PostgresProfile] = dbConfigProvider.get[PostgresProfile]
}
