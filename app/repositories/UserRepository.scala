package repositories

import javax.inject.Inject

import akka.actor.ActorSystem
import models.datastore.Models.{User, UserCreated}
import models.datastore.Tables
import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.Future


class UserRepository @Inject()(system: ActorSystem, dbConfProv: DatabaseConfigProvider)
  extends Repository(system, dbConfProv) {

  import dbConfig._
  import profile.api._

  private val users = Tables.users

  def all: Future[Seq[User]] = db.run(users.result)

  def getById(id: Long): Future[Option[User]] = db.run(users.filter(_.id === id).result.headOption)

  def getByIds(ids: Seq[Long]): Future[Seq[User]] = db.run(users.filter(_.id inSet ids).result)

  def getForUserCreated(userCreated: UserCreated): Future[User] =
    db.run(users.filter(_.id === userCreated.creatorId).result.headOption.map(_.get))

}
