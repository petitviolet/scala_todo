package models

import play.api.db._
import anorm._
import anorm.SqlParser._
import play.api.Play.current

/**
 * Created by Komurasaki on 2014/06/03.
 */
case class Task(id: Long, label: String)

object Task {
  def all(): List[Task] = DB.withConnection { implicit c =>
    SQL("SELECT * FROM task").as(task *)
  }

  def create(label: String) = {
    DB.withConnection { implicit c =>
      SQL("INSERT INTO task (label) VALUES ({label})").on(
        'label -> label
      ).executeUpdate()
    }
  }

  def delete(id: Long) = {
    DB.withConnection { implicit c =>
      SQL("DELETE FROM task WHERE id = {id}").on(
        'id -> id
      ).executeUpdate()
    }
  }

  val task = {
    get[Long]("id") ~
      get[String]("label") map {
      case id~label => Task(id, label)
    }
  }

}
