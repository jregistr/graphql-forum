package controllers

import javax.inject.Inject

import models.graphql.TypeDefinitions
import play.api.Logger
import play.api.libs.json._
import play.api.mvc._
import sangria.execution.{ErrorWithResolver, Executor, QueryAnalysisError}
import sangria.marshalling.playJson._
import sangria.parser.{QueryParser, SyntaxError}
import sangria.renderer.SchemaRenderer

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

class ApplicationController @Inject()(cc: ControllerComponents,
                                      typeDefs: TypeDefinitions)
                                     (implicit val exContext: ExecutionContext) extends AbstractController(cc) {

  private val logger = Logger(classOf[ApplicationController])

  def index = Action {
    Ok(views.html.graphiql("Interactive Forum"))
  }

  def renderSchema = Action {
    Ok(SchemaRenderer.renderSchema(typeDefs.forumSchema))
  }

  def graphQl: Action[AnyContent] = Action.async { implicit request =>
    request.body.asJson match {
      case Some(body: JsObject) =>
        val query = (body \ "query").as[String]

        val operation = (body \ "operationName").toOption.map(_.as[String])
        parseVariables(body) match {
          case Success(vars) => executeQuery(query, vars, operation)
          case Failure(_: JsResultException) => Future.successful(BadRequest(JsObject(Seq(
            "error" -> JsString("failed to read variables as a Json Object")
          ))))
          case Failure(t: _) =>
            logger.error("Error parsing variables", t)
            Future.successful(InternalServerError(JsObject(Seq(
              "error" -> JsString("Internal server error while parsing variables")
            ))))
        }
      case _ => Future {
        BadRequest(JsObject(Seq(
          "error" -> JsString("Missing request body")
        )))
      }
    }
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

  private def parseVariables(body: JsObject): Try[Option[JsObject]] = {
    val rawVars = body \ "variables"
    rawVars match {
      case _: JsUndefined => Success(None)
      case JsDefined(JsNull) => Success(None)
      case t: JsDefined => Try(Some(t.as[JsObject]))
    }
  }

}
