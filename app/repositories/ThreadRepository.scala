package repositories

import java.sql.Timestamp
import javax.inject.Inject

import akka.actor.ActorSystem
import models.datastore.Tables
import play.api.db.slick.DatabaseConfigProvider
import models.datastore.Models.{Forum, Post, Thread, User}

import scala.concurrent.Future

class ThreadRepository @Inject()(system: ActorSystem,
                                 dbConfProv: DatabaseConfigProvider,
                                 postRepository: PostRepository)
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

  def createThread(creatorId: Long, forumId: Long, name: String, content: String): Future[Thread] = {
    val createQuery = (threads returning threads
      .map(t => (t.name, t.forumId, t.creatorId, t.id, t.created, t.lastModified))) += Thread(
      name, forumId, creatorId
    )

    val res: Future[(String, Long, Long, Long, Timestamp, Timestamp)] = db.run(createQuery)
    res.flatMap(tup => {
      val thread = Thread.tupled(tup)
      postRepository.createPost(content, thread.id, thread.creatorId).map(_ => thread)
    })
  }

}
