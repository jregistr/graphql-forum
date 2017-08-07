package models.datastore

import java.sql.Timestamp

import play.api.libs.json.{Json, OFormat}

trait Node {
  val id: Int
}

trait Named {
  val name: String
}

trait TimeStamped {
  val created: Timestamp
  val lastModified: Timestamp
}

trait UserCreated {
  val creatorId: Int
}

case class User(email: String,
                firstName: Option[String] = None,
                lastName: Option[String] = None,
                id: Int = -1,
                created: Timestamp = new Timestamp(System.currentTimeMillis()),
                lastModified: Timestamp = new Timestamp(System.currentTimeMillis()))
  extends Node with TimeStamped

case class ForumGroup(name: String,
                      id: Int = -1,
                      created: Timestamp = new Timestamp(System.currentTimeMillis()),
                      lastModified: Timestamp = new Timestamp(System.currentTimeMillis()))
  extends Node with TimeStamped with Named


case class Forum(name: String,
                 groupId: Int,
                 id: Int = -1,
                 created: Timestamp = new Timestamp(System.currentTimeMillis()),
                 lastModified: Timestamp = new Timestamp(System.currentTimeMillis()))
  extends Node with Named with TimeStamped


case class Thread(name: String,
                  forumId: Int,
                  creatorId: Int,
                  id: Int = -1,
                  created: Timestamp = new Timestamp(System.currentTimeMillis()),
                  lastModified: Timestamp = new Timestamp(System.currentTimeMillis()))
  extends Node with UserCreated with Named with TimeStamped


case class Post(text: String,
                creatorId: Int,
                replyingToId: Option[Int],
                id: Int = -1,
                created: Timestamp = new Timestamp(System.currentTimeMillis()),
                lastModified: Timestamp = new Timestamp(System.currentTimeMillis()))
  extends Node with UserCreated with TimeStamped

object User {
  implicit val format: OFormat[User] = Json.format[User]
}

object ForumGroup {
  implicit val format: OFormat[ForumGroup] = Json.format[ForumGroup]
}

object Forum {
  implicit val format: OFormat[Forum] = Json.format[Forum]
}

object Thread {
  implicit val format: OFormat[Thread] = Json.format[Thread]
}

object Post {
  implicit val format: OFormat[Post] = Json.format[Post]
}