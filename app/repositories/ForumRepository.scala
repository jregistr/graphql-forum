package repositories

import javax.inject.Inject

import akka.actor.ActorSystem
import models.datastore.Models.{Forum, ForumGroup, Thread}
import models.datastore.Tables
import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.Future

class ForumRepository @Inject()(system: ActorSystem, dbConfProv: DatabaseConfigProvider)
  extends Repository(system, dbConfProv) {

  import dbConfig._
  import profile.api._

  private val forums = Tables.forums

  def all: Future[Seq[Forum]] = db.run(forums.result)

  def getById(id: Long): Future[Option[Forum]] = db.run(forums.filter(_.id === id).result.headOption)

  def getAllByIds(ids: Seq[Long]): Future[Seq[Forum]] = db.run(forums.filter(_.id inSet ids).result)

  def getAllByForumGroup(group: ForumGroup): Future[Seq[Forum]] = db.run(forums.filter(_.groupId === group.id).result)

  def getForThread(thread: Thread): Future[Forum] =
    db.run(forums.filter(_.id === thread.forumId).result.headOption.map(_.get))

}
