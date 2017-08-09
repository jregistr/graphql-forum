package repositories

import javax.inject.Inject

import akka.actor.ActorSystem
import models.datastore.Models.{Forum, ForumGroup}
import models.datastore.Tables
import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.Future

class ForumGroupsRepository @Inject()(system: ActorSystem, dbConfProv: DatabaseConfigProvider)
  extends Repository(system, dbConfProv) {

  import dbConfig._
  import profile.api._

  private val groups = Tables.forumGroups

  def all: Future[Seq[ForumGroup]] = db.run(groups.result)

  def getById(id: Long): Future[Option[ForumGroup]] = db.run(groups.filter(_.id === id).result.headOption)

  def getByIds(ids: Seq[Long]): Future[Seq[ForumGroup]] = db.run(groups.filter(_.id inSet ids).result)

  def getForForum(forum: Forum): Future[ForumGroup] =
    db.run(groups.filter(_.id === forum.groupId).result.headOption.map(_.get))

}
