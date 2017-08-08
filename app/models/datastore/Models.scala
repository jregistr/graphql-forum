package models.datastore

import java.sql.Timestamp
import java.text.SimpleDateFormat

import org.joda.time.DateTime
import play.api.libs.json._

object Models {

  sealed trait Node {
    val id: Long
  }

  sealed trait Named {
    val name: String
  }

  sealed trait TimeStamped {
    val created: Timestamp
    val lastModified: Timestamp
  }

  sealed trait UserCreated {
    val creatorId: Long
  }

  case class User(email: String,
                  firstName: Option[String] = None,
                  lastName: Option[String] = None,
                  id: Long = -1,
                  created: Timestamp = new Timestamp(System.currentTimeMillis()),
                  lastModified: Timestamp = new Timestamp(System.currentTimeMillis()))
    extends Node with TimeStamped

  case class ForumGroup(name: String,
                        id: Long = -1,
                        created: Timestamp = new Timestamp(System.currentTimeMillis()),
                        lastModified: Timestamp = new Timestamp(System.currentTimeMillis()))
    extends Node with TimeStamped with Named


  case class Forum(name: String,
                   groupId: Long,
                   id: Long = -1,
                   created: Timestamp = new Timestamp(System.currentTimeMillis()),
                   lastModified: Timestamp = new Timestamp(System.currentTimeMillis()))
    extends Node with Named with TimeStamped


  case class Thread(name: String,
                    forumId: Long,
                    creatorId: Long,
                    id: Long = -1,
                    created: Timestamp = new Timestamp(System.currentTimeMillis()),
                    lastModified: Timestamp = new Timestamp(System.currentTimeMillis()))
    extends Node with UserCreated with Named with TimeStamped


  case class Post(content: String,
                  threadId: Long,
                  creatorId: Long,
                  replyingToId: Option[Long],
                  id: Long = -1,
                  created: Timestamp = new Timestamp(System.currentTimeMillis()),
                  lastModified: Timestamp = new Timestamp(System.currentTimeMillis()))
    extends Node with UserCreated with TimeStamped

}