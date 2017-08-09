package controllers

import javax.inject.Inject

import models.graphql.TypeDefinitions
import play.api.mvc._

import scala.concurrent.ExecutionContext

class ApplicationController @Inject()(cc: ControllerComponents,
                                      typeDefs: TypeDefinitions)
                                     (implicit val exContext: ExecutionContext) extends AbstractController(cc) {
  def index = Action {
    Ok("HELLO THERE!!!")
  }

}
