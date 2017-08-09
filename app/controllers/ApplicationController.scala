package controllers

import javax.inject.Inject

import models.graphql.TypeDefinitions
import play.api.libs.json.{JsNumber, JsObject, JsString, Json}
import play.api.mvc._
import sangria.execution.{ErrorWithResolver, Executor, QueryAnalysisError}
import sangria.marshalling.playJson._
import sangria.parser.{QueryParser, SyntaxError}
import sangria.renderer.SchemaRenderer

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class ApplicationController @Inject()(cc: ControllerComponents,
                                      typeDefs: TypeDefinitions)
                                     (implicit val exContext: ExecutionContext) extends AbstractController(cc) {
  def index = Action {
    Ok("Not yet implemented")
  }

  def renderSchema = Action {
    Ok(SchemaRenderer.renderSchema(typeDefs.forumSchema))
  }

  def graphQl(query: String, variables: Option[String], operation: Option[String]): Action[AnyContent] = Action.async {
    val vars = variables.map(parseVariables)
    executeQuery(query, vars, operation)
  }

  private def executeQuery(query: String, vars: Option[JsObject], operation: Option[String]) =
    QueryParser.parse(query) match {
      case Success(ast) =>
        Executor.execute(
          typeDefs.forumSchema, ast, typeDefs,
          operationName = operation,
          variables = vars.getOrElse(Json.obj())
        ).map(Ok(_))
          .recover {
            case e: QueryAnalysisError => BadRequest(e.resolveError)
            case e: ErrorWithResolver => InternalServerError(e.resolveError)
          }
      case Failure(syntaxError: SyntaxError) => Future.successful(
        BadRequest(JsObject(Seq(
          "syntaxError" -> JsString(syntaxError.getMessage()),
          "location" -> JsObject(Seq(
            "line" -> JsNumber(syntaxError.originalError.position.line),
            "column" -> JsNumber(syntaxError.originalError.position.column)
          ))
        )))
      )
      case Failure(e) => throw e
    }

  private def parseVariables(vars: String) =
    if(vars.trim == "" || vars.trim == "null") Json.obj() else Json.parse(vars).as[JsObject]

}
