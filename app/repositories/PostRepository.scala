package repositories

import javax.inject.Inject

import akka.actor.ActorSystem
import models.datastore.Models.{Post, Thread, User}
import models.datastore.Tables
import play.api.db.slick.DatabaseConfigProvider

import scala.concurrent.Future

class PostRepository @Inject()(system: ActorSystem, dbConfProv: DatabaseConfigProvider)
  extends Repository(system, dbConfProv) {

  import dbConfig._
  import profile.api._

  private val posts = Tables.posts

  def all: Future[Seq[Post]] = db.run(posts.result)

  def getById(id: Long): Future[Option[Post]] = db.run(posts.filter(_.id === id).result.headOption)

  def getByIds(ids: Seq[Long]): Future[Seq[Post]] = db.run(posts.filter(_.id inSet ids).result)

  def getForThread(thread: Thread): Future[Seq[Post]] = db.run(posts.filter(_.threadId === thread.id).result)

  def getForUser(user: User): Future[Seq[Post]] = db.run(posts.filter(_.creatorId === user.id).result)

}
