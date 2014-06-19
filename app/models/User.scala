package models
/**
 * Created by Komurasaki on 2014/06/18.
 */

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._
import org.joda.time._
import models.AnormDateExtension._

case class User(id: Long,
                email: String,
                name: String,
                password: String,
                created: DateTime,
                modified: DateTime)

object User {
  val user = {
    get[Long]("id") ~
    get[String]("user.email") ~
      get[String]("user.name") ~
      get[String]("user.password") ~
      get[DateTime]("user.created") ~
      get[DateTime]("user.modified") map {
      case id~email ~ name ~ password ~ created ~ modified => {
        User(id, email, name, password, created, modified)
      }
    }
  }

  def findById(id: Long): Option[User] = {
    DB.withConnection { implicit c =>
      SQL("select * from user where id = {id}").on(
        'id -> id
      ).as(user.singleOpt)
    }
  }

  def findByEmail(email: String): Option[User] = {
    DB.withConnection { implicit c =>
      SQL("select * from user where email = {email}").on(
        'email -> email
      ).as(user.singleOpt)
    }
  }
  def findAll: Seq[User] = {
    DB.withConnection { implicit c =>
      SQL("select * from user").as(user *)
    }
  }

  def authenticate(email: String, password: String): Option[User] = {
    DB.withConnection { implicit c =>
      SQL(
        """
          select * from user where
          email = {email} and password = {password}
        """
      ).on(
          'email -> email,
          'password -> password
        ).as(user.singleOpt)
    }
  }

  def create(email: String, name: String, password: String,
             created: DateTime, modified: DateTime): Option[User] = {
    DB.withConnection { implicit c =>
      val userId = SQL(
        """
          insert into user (
            email, name, password, created, modified
          )
          values (
            {email}, {name}, {password}, {created}, {modified}
          )
        """
      ).on(
          'email -> email,
          'name -> name,
          'password -> password,
          'created -> created,
          'modified -> modified
        ).executeInsert()
      userId match {
        case Some(userId) => User.findById(userId)
        case _ => None
      }
    }
  }
}