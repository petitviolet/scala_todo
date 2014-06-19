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
case class UserTask(
                     id: Long, user_id: Long, task_id: Long,
                created: DateTime, modified: DateTime)

object UserTask {
  def userAll(user: User): List[Task] = {

    DB.withConnection { implicit c =>
      Logger.debug("UserTask.userAll: " + user.toString())
      SQL("SELECT task.* FROM task, user, user_task " +
        "where task.id = user_task.task_id " +
        "and user.id = user_task.user_id " +
        "and user.email = {email}")
        .on('email -> user.email).as(Task.task *)
    }
  }
  def all(): List[UserTask] = DB.withConnection { implicit c =>
    SQL("select * from user_task").as(userTask *)
  }

  def create(userId: Long, taskId: Long) {
    val now = new DateTime()
    DB.withConnection { implicit c => {
      SQL("INSERT INTO user_task (user_id, task_id, created, modified) " +
        "VALUES ({user_id}, {task_id}, {created}, {modified})").on(
          'user_id -> userId,
          'task_id -> taskId,
          'created -> now, 'modified -> now
        ).executeInsert()
    }}
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

  val userTask = {
    get[Long]("id") ~
      get[Long]("userId") ~
      get[Long]("taskId") ~
      get[DateTime]("created") ~
      get[DateTime]("modified") map {
      case id~userId~taskId~created~modified => {
        UserTask(id, userId, taskId, created, modified)
      }
    }
  }

}
