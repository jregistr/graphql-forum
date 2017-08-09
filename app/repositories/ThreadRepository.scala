package repositories

import javax.inject.Inject

import akka.actor.ActorSystem
import models.datastore.Tables
import play.api.db.slick.DatabaseConfigProvider
import models.datastore.Models.{Forum, Post, Thread, User}

import scala.concurrent.Future

class ThreadRepository @Inject()(system: ActorSystem, dbConfProv: DatabaseConfigProvider)
  extends Repository(system, dbConfProv) {

  import dbConfig._
  import profile.api._

  private val threads = Tables.threads

  def all: Future[Seq[Thread]] = db.run(threads.result)

  def getById(id: Long): Future[Option[Thread]] = db.run(threads.filter(_.id === id).result.headOption)

  def getAllByIds(ids: Seq[Long]): Future[Seq[Thread]] = db.run(threads.filter(_.id inSet ids).result)

  def getAllByForum(forum: Forum): Future[Seq[Thread]] = db.run(threads.filter(_.forumId === forum.id).result)

  def getAllByUser(user: User): Future[Seq[Thread]] = db.run(threads.filter(_.creatorId === user.id).result)

  def getForPost(post: Post): Future[Thread] =
    db.run(threads.filter(_.id === post.threadId).result.headOption.map(_.get))

}
