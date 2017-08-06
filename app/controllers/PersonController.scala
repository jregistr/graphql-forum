package controllers

import javax.inject._

import play.api.i18n._
import play.api.mvc._

import scala.concurrent.ExecutionContext

class PersonController @Inject()(cc: ControllerComponents
                                )(implicit ec: ExecutionContext)
  extends AbstractController(cc) with I18nSupport {

  /**
   * The index action.
   */
  def index = Action { implicit request =>
    Ok(views.html.index("Hello"))
  }

}

