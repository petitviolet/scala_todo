package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import org.joda.time.DateTime
import models.AnormDateExtension._
import play.Logger

/**
 * Created by Komurasaki on 2014/06/03.
 */
case class Task(id: Long, label: String,
                 created: DateTime, modified: DateTime)

object Task {
  def userAll(user: User): Seq  [Task] = {
    Logger.debug("Task.userAll: " + user.toString)
    DB.withConnection { implicit c =>
      SQL("SELECT task.label FROM task, user, user_task " +
        "where task.id = user_task.task_id " +
        "and user.id = user_task.user_id " +
        "and user.email ").as(task *)
    }
  }
  def all(): List[Task] = DB.withConnection { implicit c =>
    SQL("select * from task").as(task *)
  }

  def create(label: String, email: String) {
    User.findByEmail(email) .map {
      user => {
        Logger.debug("user data: " + user.toString())
        DB.withConnection {implicit c => {
          val now = new DateTime()
          val taskId = SQL("INSERT INTO task (label, created, modified) " +
            "VALUES ({label}, {created}, {modified})").on(
              'label -> label,
              'created -> now,'modified -> now
            ).executeInsert(scalar[Long].single)
          UserTask.create(user.id, taskId)
        }
        }
      }
    }

  }

  def delete(id: Long) {
    DB.withConnection { implicit c => {
      SQL("DELETE FROM task WHERE id = {id}").on(
        'id -> id
      ).executeUpdate()
      SQL("DELETE FROM user_task WHERE task_id = {id}").on(
        'id -> id
      ).executeUpdate()
    }}
  }

  val task = {
    get[Long]("id") ~
      get[String]("label") ~
      get[DateTime]("created") ~
      get[DateTime]("modified") map {
      case id~label~created~modified => {
        Task(id, label, created, modified)
      }
    }
  }

}
