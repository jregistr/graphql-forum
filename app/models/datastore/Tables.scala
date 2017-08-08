package models.datastore

import java.sql.Timestamp

import slick.jdbc.PostgresProfile.api._
import slick.lifted.ProvenShape

sealed trait NodeTable {
  this: Table[_] =>

  def id: Rep[Long] = column[Long]("id", O.PrimaryKey, O.AutoInc)
}

sealed trait TimeStampedTable {
  this: Table[_] =>

  def created: Rep[Timestamp] = column[Timestamp]("date_created")

  def lastModified: Rep[Timestamp] = column[Timestamp]("last_modified")
}

sealed trait NamedTable {
  this: Table[_] =>

  def name: Rep[String] = column[String]("name", O.Unique)
}

sealed trait UserCreatedTable {
  this: Table[_] =>

  def creatorId: Rep[Long] = column[Long]("created_by")

  def fkCreatorId = foreignKey("fk_user_id", creatorId, Tables.users)(_.id,
    onUpdate = ForeignKeyAction.Restrict,
    onDelete = ForeignKeyAction.Cascade)
}

object Tables {
  val users: TableQuery[UsersTable] = TableQuery[UsersTable]
  val forumGroups: TableQuery[ForumGroupsTable] = TableQuery[ForumGroupsTable]
  val forums: TableQuery[ForumsTable] = TableQuery[ForumsTable]
  val threads: TableQuery[ThreadsTable] = TableQuery[ThreadsTable]
  val posts: TableQuery[PostsTable] = TableQuery[PostsTable]
}

class UsersTable(tag: Tag) extends Table[User](tag, "users") with NodeTable with TimeStampedTable {

  def email: Rep[String] = column[String]("email", O.Unique)

  def firstName: Rep[Option[String]] = column[Option[String]]("first_name")

  def lastName: Rep[Option[String]] = column[Option[String]]("last_name")

  override def * : ProvenShape[User] =
    (email, firstName, lastName, id, created, lastModified) <> ((User.apply _).tupled, User.unapply)
}

class ForumGroupsTable(tag: Tag) extends Table[ForumGroup](tag, "forum_groups") with NodeTable
  with TimeStampedTable with NamedTable {
  override def * : ProvenShape[ForumGroup] =
    (name, id, created, lastModified) <> ((ForumGroup.apply _).tupled, ForumGroup.unapply)
}

class ForumsTable(tag: Tag) extends Table[Forum](tag, "forums") with NodeTable with TimeStampedTable with NamedTable {

  def groupId: Rep[Long] = column[Long]("group_id")

  def fkGroupId = foreignKey("fk_group_id", groupId, Tables.forumGroups)(_.id,
    onUpdate = ForeignKeyAction.Restrict, onDelete = ForeignKeyAction.Cascade)

  override def * : ProvenShape[Forum] =
    (name, groupId, id, created, lastModified) <> ((Forum.apply _).tupled, Forum.unapply)
}

class ThreadsTable(tag: Tag) extends Table[Thread](tag, "threads") with NodeTable
  with TimeStampedTable with NamedTable with UserCreatedTable {

  def forumId: Rep[Long] = column[Long]("forum")

  def fkForumId = foreignKey("fk_forum_id", forumId, Tables.forums)(_.id, onUpdate = ForeignKeyAction.Restrict,
    onDelete = ForeignKeyAction.Cascade)

  override def * : ProvenShape[Thread] =
    (name, forumId, creatorId, id, created, lastModified) <> ((Thread.apply _).tupled, Thread.unapply)

}

class PostsTable(tag: Tag) extends Table[Post](tag, "posts") with NodeTable with TimeStampedTable
  with NamedTable with UserCreatedTable {

  def threadId: Rep[Long] = column[Long]("thread_id")

  def content: Rep[String] = column[String]("content")

  def replyingToId: Rep[Option[Long]] = column[Option[Long]]("replying_to")

  def fkReplyingToId = foreignKey("fk_replying_to_id", replyingToId, Tables.users)(_.id.?,
    onUpdate = ForeignKeyAction.SetNull,
    onDelete = ForeignKeyAction.SetNull)

  def fkThreadId = foreignKey("fk_thread_id", threadId, Tables.threads)(_.id,
    onUpdate = ForeignKeyAction.Restrict,
    onDelete = ForeignKeyAction.Cascade)

  override def * : ProvenShape[Post] =
    (content, threadId, creatorId, replyingToId, id, created, lastModified) <> ((Post.apply _).tupled, Post.unapply)
}