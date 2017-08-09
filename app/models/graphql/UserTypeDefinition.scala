package models.graphql

import javax.inject.Inject

import models.datastore.Models.User
import repositories.{PostRepository, ThreadRepository, UserRepository}
import sangria.schema._

import scala.concurrent.ExecutionContext

class UserTypeDefinition @Inject()(interfacesDefs: InterfaceTypeDefinitions,
                                   userRepository: UserRepository,
                                   threadRepository: ThreadRepository,
                                   postRepository: PostRepository)
                                  (implicit val executionContext: ExecutionContext) {

  val userType: ObjectType[Unit, User] = ObjectType(
    "User",
    "A user on this forum",
    interfaces[Unit, User](interfacesDefs.nodeInterface, interfacesDefs.timeStampedInterface),
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
      )
    )
  )

}
