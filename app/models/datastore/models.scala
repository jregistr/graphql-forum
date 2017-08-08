package models.datastore

import java.sql.Timestamp
import java.text.SimpleDateFormat

import org.joda.time.DateTime
import play.api.libs.json._

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

private object Formats {
  val timeStampFormat: Format[Timestamp] = {
    val dtFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
    new Format[Timestamp] {
      override def reads(json: JsValue): JsResult[Timestamp] = json match {
        case JsNumber(value) => JsSuccess(new Timestamp(value.toLong))
        case JsString(value) => JsSuccess(new Timestamp(DateTime.parse(value).getMillis))
        case _ => JsError(s"Unexpected time format for $json")
      }

      override def writes(o: Timestamp): JsValue = JsString(dtFormat.format(o))
    }
  }
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

object User {
  implicit val tFormat: Format[Timestamp] = Formats.timeStampFormat
  implicit val format: OFormat[User] = Json.format[User]
}

object ForumGroup {
  implicit val tFormat: Format[Timestamp] = Formats.timeStampFormat
  implicit val format: OFormat[ForumGroup] = Json.format[ForumGroup]
}

object Forum {
  implicit val tFormat: Format[Timestamp] = Formats.timeStampFormat
  implicit val format: OFormat[Forum] = Json.format[Forum]
}

object Thread {
  implicit val tFormat: Format[Timestamp] = Formats.timeStampFormat
  implicit val format: OFormat[Thread] = Json.format[Thread]
}

object Post {
  implicit val tFormat: Format[Timestamp] = Formats.timeStampFormat
  implicit val format: OFormat[Post] = Json.format[Post]
}