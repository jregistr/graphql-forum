package models.graphql

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

import models.datastore.Models.{Node, TimeStamped}
import sangria.schema._

import scala.concurrent.ExecutionContext

class InterfaceTypeDefinitions @Inject()(private implicit val executionContext: ExecutionContext) {

  private val dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss.SSS")

  val nodeInterface: InterfaceType[Unit, Node] =
    InterfaceType(
      "Node",
      "Base interface representing an object with an ID",
      fields[Unit, Node](
        Field("id",
          IntType,
          Some("unique incrementing identifier for this Node"),
          resolve = _.value.id
        )
      )
    )

  val namedInterface: InterfaceType[Unit, models.datastore.Models.Named] =
    InterfaceType(
      "Named",
      "An object with a unique name",
      fields[Unit, models.datastore.Models.Named](
        Field(
          "name",
          StringType,
          Some("The unique name of this object"),
          resolve = _.value.name
        )
      )
    )

  val timeStampedInterface: InterfaceType[Unit, TimeStamped] =
    InterfaceType(
      "TimeStamped",
      "An object with timestamp for date created and last modified fields",
      fields[Unit, TimeStamped](
        Field(
          "created",
          StringType,
          Some("The date the object was created"),
          resolve = ctx => stampToString(ctx.value.created)
        ),
        Field(
          "lastModified",
          StringType,
          Some("The date this object's data was modified"),
          resolve = ctx => stampToString(ctx.value.lastModified)
        )
      )
    )

  private def stampToString(timeStamp: Timestamp) = dateFormat.format(new Date(timeStamp.getTime))

}
