package models.graphql

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

import models.datastore.Models.{Node, TimeStamped, Named => NamedModel}
import sangria.schema._

import scala.concurrent.ExecutionContext

class InterfaceTypeDefinitions @Inject()(private implicit val executionContext: ExecutionContext) {

  private val dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss.SSS")

  val nodeInterface: InterfaceType[Unit, Node] =
    InterfaceType(
      "Node",
      "Base interface representing an object with an ID",
      fields[Unit, Node](
        idField[Node]
      )
    )

  val namedInterface: InterfaceType[Unit, NamedModel] =
    InterfaceType(
      "Named",
      "An object with a unique name",
      fields[Unit, NamedModel](
        namedField[NamedModel]
      )
    )

  val timeStampedInterface: InterfaceType[Unit, TimeStamped] =
    InterfaceType(
      "TimeStamped",
      "An object with timestamp for date created and last modified fields",
      fields[Unit, TimeStamped](
        createdField[TimeStamped],
        lastModifiedField[TimeStamped]
      )
    )

  def idField[T <: Node]: Field[Unit, T] = Field(
    "id",
    LongType,
    Some("unique incrementing identifier for this Node"),
    resolve = _.value.id
  )

  def namedField[T <: NamedModel]: Field[Unit, T] = Field(
    "name",
    StringType,
    Some("The unique name of this object"),
    resolve = _.value.name
  )

  def createdField[T <: TimeStamped]: Field[Unit, T] = Field(
    "created",
    StringType,
    Some("The date the object was created"),
    resolve = ctx => stampToString(ctx.value.created)
  )

  def lastModifiedField[T <: TimeStamped]: Field[Unit, T] = Field(
    "lastModified",
    StringType,
    Some("The date this object's data was modified"),
    resolve = ctx => stampToString(ctx.value.lastModified)
  )

  private def stampToString(timeStamp: Timestamp) = dateFormat.format(new Date(timeStamp.getTime))

}
