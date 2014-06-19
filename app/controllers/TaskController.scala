package controllers

import models._
import play.api.data._
import play.api.mvc._
import play.api.data.Forms._


/**
 * Created by Komurasaki on 2014/06/18.
 */
object TaskController extends Controller with Secured {

  def tasks = Action { implicit request =>
    session.get("email").map {
      email => {
        User.findByEmail(email).map {
          user => Ok(views.html.user_task(UserTask.userAll(user), taskForm))
        } .getOrElse {Redirect(routes.UserController.login())}
      }
    }.getOrElse{Redirect(routes.UserController.login())}
  }


  def allTasks = Action {
    Ok(views.html.task_index(Task.all(), taskForm))
  }

  def newTask = Action{ implicit request =>
    taskForm.bindFromRequest.fold(
      errors => BadRequest(views.html.task_index(Task.all(), errors)),
      label => {
        session.get("email").map {
          email => Task.create(label, email)
        }
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
