package repositories

import javax.inject.Inject

import akka.actor.ActorSystem
import com.google.inject.ImplementedBy
import models.datastore.Models.{Forum, ForumGroup}
import models.datastore.Tables
import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.Future

@ImplementedBy(classOf[ForumRepository])
class ForumRepository @Inject()(system: ActorSystem, dbConfProv: DatabaseConfigProvider)
  extends Repository(system, dbConfProv) {

  import dbConfig._
  import profile.api._

  private val forums = Tables.forums

  def all: Future[Seq[Forum]] = db.run(forums.result)

  def getById(id: Long): Future[Option[Forum]] = db.run(forums.filter(_.id === id).result.headOption)

  def getByIds(ids: Seq[Long]): Future[Seq[Forum]] = db.run(forums.filter(_.id inSet ids).result)

  def getByForumGroup(group: ForumGroup): Future[Seq[Forum]] = db.run(forums.filter(_.groupId === group.id).result)

}
