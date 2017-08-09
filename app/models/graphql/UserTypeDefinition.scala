package models.graphql

import javax.inject.Inject

import models.datastore.Models.User
import repositories.UserRepository
import sangria.schema._

import scala.concurrent.ExecutionContext

class UserTypeDefinition @Inject()(interfacesDefs: InterfaceTypeDefinitions,
                                   userRepository: UserRepository)
                                  (implicit val executionContext: ExecutionContext) {

  val userType: ObjectType[Unit, User] = ObjectType(
    "User",
    "A user on this forum",
    interfaces[Unit, User](interfacesDefs.nodeInterface, interfacesDefs.timeStampedInterface),
    fields[Unit, User](
      Field(
        "id",
        IntType,
        resolve = _.value.id
      )
    )
  )

}
