package repositories

import javax.inject.Inject

import akka.actor.ActorSystem
import com.google.inject.ImplementedBy
import models.datastore.Models.User
import models.datastore.Tables
import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.Future

@ImplementedBy(classOf[UserRepository])
class UserRepository @Inject()(system: ActorSystem, dbConfProv: DatabaseConfigProvider)
  extends Repository(system, dbConfProv) {

  import dbConfig._
  import profile.api._

  private val users = Tables.users

  def all: Future[Seq[User]] = db.run(users.result)

  def getById(id: Long): Future[Option[User]] = db.run(users.filter(_.id === id).result.headOption)

  def getByIds(ids: Seq[Long]): Future[Seq[User]] = db.run(users.filter(_.id inSet ids).result)

}
