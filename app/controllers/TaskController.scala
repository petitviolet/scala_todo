package controllers

import models.Task
import play.api.data._
import play.api.mvc._
import play.api.data.Forms._

/**
 * Created by Komurasaki on 2014/06/18.
 */
object TaskController extends Controller {
  def test(message: String) = Action {
    Ok(views.html.test(message))
  }

  def index = Action {
    Ok(views.html.index(Task.all(), taskForm))

  }

  def tasks = Action {
    Ok(views.html.index(Task.all(), taskForm))
  }

  def newTask = Action{ implicit request =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest(views.html.index(Task.all(), errors)),
      label => {
        Task.create(label)
        Redirect(routes.TaskController.tasks)
      }
    )
  }

  def deleteTask(id: Long) = Action {implicit request =>
    Task.delete(id)
    Redirect(routes.TaskController.tasks)
  }

  val taskForm = Form(
    "label" -> nonEmptyText
  )
}