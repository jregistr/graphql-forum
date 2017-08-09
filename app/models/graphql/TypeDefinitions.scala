package models.graphql

import javax.inject.Inject

import models.datastore.Models.{Forum, ForumGroup, Post, Thread, User, UserCreated}
import repositories._
import sangria.schema._

import scala.concurrent.{ExecutionContext, Future}

class TypeDefinitions @Inject()(val interfacesDefs: InterfaceTypeDefinitions,
                                val userRepository: UserRepository,
                                val forumGroupsRepo: ForumGroupsRepository,
                                val forumRepository: ForumRepository,
                                val threadRepository: ThreadRepository,
                                val postRepository: PostRepository)
                               (implicit val executionContext: ExecutionContext) {

  lazy val UserCreatedInterface: InterfaceType[Unit, UserCreated] =
    InterfaceType(
      "UserCreated",
      "An object that is created by a user.",
      fields[Unit, UserCreated](
        createdByField[UserCreated]
      )
    )

  lazy val UserDefinition: ObjectType[Unit, User] = ObjectType(
    "User",
    "A user on this forum",
    interfaces[Unit, User](interfacesDefs.NodeInterface, interfacesDefs.TimeStampedInterface),
    fields[Unit, User](
      interfacesDefs.idField[User],
      interfacesDefs.createdField[User],
      interfacesDefs.lastModifiedField[User],

      Field(
        "email",
        StringType,
        Some("The email address of this user"),
        resolve = _.value.email
      ),
      Field(
        "firstName",
        OptionType(StringType),
        Some("The user's first name"),
        resolve = _.value.firstName
      ),
      Field(
        "lastName",
        OptionType(StringType),
        Some("The user's last name"),
        resolve = _.value.lastName
      ),
      Field(
        "threads",
        ListType(ThreadDefinition),
        Some("The threads created by this user"),
        resolve = ctx => threadRepository.getAllByUser(ctx.value)
      ),
      Field(
        "posts",
        ListType(PostDefinition),
        Some("The posts made by this user"),
        resolve = ctx => postRepository.getForUser(ctx.value)
      )
    )
  )

  lazy val ForumGroupDefinition: ObjectType[Unit, ForumGroup] = ObjectType(
    "ForumGroup",
    "A group of common forums",
    interfaces[Unit, ForumGroup](
      interfacesDefs.NodeInterface,
      interfacesDefs.TimeStampedInterface,
      interfacesDefs.NamedInterface
    ),

    fields[Unit, ForumGroup](
      interfacesDefs.idField[ForumGroup],
      interfacesDefs.namedField[ForumGroup],
      interfacesDefs.createdField[ForumGroup],
      interfacesDefs.lastModifiedField[ForumGroup],

      Field(
        "forums",
        ListType(ForumDefinition),
        Some("Forums that belong to this group"),
        resolve = ctx => forumRepository.getAllByForumGroup(ctx.value)
      )
    )
  )

  lazy val ForumDefinition: ObjectType[Unit, Forum] = ObjectType(
    "Forum",
    "A forum for threads",
    interfaces[Unit, Forum](
      interfacesDefs.NodeInterface,
      interfacesDefs.TimeStampedInterface,
      interfacesDefs.NamedInterface
    ),

    fields[Unit, Forum](
      interfacesDefs.idField[Forum],
      interfacesDefs.createdField[Forum],
      interfacesDefs.lastModifiedField[Forum],
      interfacesDefs.namedField[Forum],

      Field(
        "forumGroup",
        ForumGroupDefinition,
        Some("The forum group this forum belongs to."),
        resolve = ctx => forumGroupsRepo.getForForum(ctx.value)
      ),

      Field(
        "threads",
        ListType(ThreadDefinition),
        Some("The threads belonging to this forum"),
        resolve = ctx => threadRepository.getAllByForum(ctx.value)
      )
    )
  )

  lazy val ThreadDefinition: ObjectType[Unit, Thread] = ObjectType(
    "Thread",
    "A discussion thread created by a user",
    interfaces[Unit, Thread](
      interfacesDefs.NodeInterface,
      interfacesDefs.TimeStampedInterface,
      interfacesDefs.NamedInterface,
      UserCreatedInterface
    ),

    fields[Unit, Thread](
      interfacesDefs.idField[Thread],
      interfacesDefs.createdField[Thread],
      interfacesDefs.lastModifiedField[Thread],
      interfacesDefs.namedField[Thread],
      createdByField[Thread],

      Field(
        "forum",
        ForumDefinition,
        Some("The forum this thread belongs to."),
        resolve = ctx => forumRepository.getForThread(ctx.value)
      ),
      Field(
        "posts",
        ListType(PostDefinition),
        Some("The posts that have been made to this thread."),
        resolve = ctx => postRepository.getForThread(ctx.value)
      )
    )
  )

  lazy val PostDefinition: ObjectType[Unit, Post] = ObjectType(
    "Post",
    "A post made to a thread by a user",
    interfaces[Unit, Post](
      interfacesDefs.NodeInterface,
      interfacesDefs.TimeStampedInterface,
      UserCreatedInterface
    ),

    fields[Unit, Post](
      interfacesDefs.idField[Post],
      interfacesDefs.createdField[Post],
      interfacesDefs.lastModifiedField[Post],
      createdByField[Post],

      Field(
        "content",
        StringType,
        resolve = _.value.content
      ),

      Field(
        "thread",
        ThreadDefinition,
        Some("The thread this post belongs to."),
        resolve = ctx => threadRepository.getForPost(ctx.value)
      ),

      Field(
        "replyingTo",
        OptionType(PostDefinition),
        Some("The post this one is replying to."),
        resolve = ctx => ctx.value.replyingToId match {
          case Some(targetId) => postRepository.getById(targetId)
          case _ => Future.successful(None)
        }
      )
    )
  )

  lazy val idArgument: Argument[Long] = Argument(
    "id",
    LongType
  )

  lazy val QueryDefinition: ObjectType[Unit, Unit] = ObjectType(
    "query",
    fields[Unit, Unit](
      Field(
        "users",
        ListType(UserDefinition),
        arguments = Nil,
        resolve = _ => userRepository.all
      ),
      Field(
        "user",
        OptionType(UserDefinition),
        arguments = List(idArgument),
        resolve = ctx => userRepository.getById(ctx.arg[Long](idArgument))
      ),
      Field(
        "forumGroups",
        ListType(ForumGroupDefinition),
        arguments = Nil,
        resolve = _ => forumGroupsRepo.all
      ),
      Field(
        "forumGroup",
        OptionType(ForumGroupDefinition),
        arguments = List(idArgument),
        resolve = ctx => forumGroupsRepo.getById(ctx.arg(idArgument))
      ),
      Field(
        "forums",
        ListType(ForumDefinition),
        arguments = Nil,
        resolve = _ => forumRepository.all
      ),
      Field(
        "forum",
        OptionType(ForumDefinition),
        arguments = List(idArgument),
        resolve = ctx => forumRepository.getById(ctx.arg(idArgument))
      )
    )
  )

  lazy val forumSchema = Schema(QueryDefinition)

  private def createdByField[T <: UserCreated]: Field[Unit, T] = Field(
    "createdBy",
    UserDefinition,
    Some("The user that created this thread"),
    resolve = ctx => userRepository.getForUserCreated(ctx.value)
  )

}
