package models.datastore

import slick.jdbc.H2Profile.api._
import slick.lifted.ProvenShape

private object TableConstants {
  val Id = "id"
  val Created = "date_created"
  val LastModified = "last_modified"
}

import TableConstants._

class UsersTable(tag: Tag) extends Table[User](tag, "users") {
  def email: Rep[String] = column[String]("email")

  def firstName: Rep[Option[String]] = column[Option[String]]("first_name")

  def lastName: Rep[Option[String]] = column[Option[String]]("last_name")

  override def * : ProvenShape[User] = ???
}

class ForumGroupsTable(tag: Tag) extends Table[ForumGroup](tag, "forum_groups") {
  override def * : ProvenShape[ForumGroup] = ???
}

class ForumsTable(tag: Tag) extends Table[Forum](tag, "forums") {
  override def * : ProvenShape[Forum] = ???
}

class ThreadsTable(tag: Tag) extends Table[Thread](tag, "threads") {
  override def * : ProvenShape[Thread] = ???
}

class PostsTable(tag: Tag) extends Table[Post](tag, "posts") {
  override def * : ProvenShape[Post] = ???
}