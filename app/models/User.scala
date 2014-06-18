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
import java.math.BigInteger

case class User(email: String,
                name: String,
                password: String,
                created: DateTime,
                modified: DateTime)

object User {
  val simple = {
    get[String]("user.email") ~
      get[String]("user.name") ~
      get[String]("user.password") ~
      get[DateTime]("user.created") ~
      get[DateTime]("user.modified") map {
      case email ~ name ~ password ~ created ~ modified => {
        User(email, name, password, created, modified)
      }
    }
  }

  def findById(id: Long): Option[User] = {
    DB.withConnection { implicit c =>
      SQL("select * from user where user_id = {id}").on(
        'id -> id
      ).as(User.simple.singleOpt)
    }
  }

  def findByEmail(email: String): Option[User] = {
    DB.withConnection { implicit c =>
      SQL("select * from user where email = {email}").on(
        'email -> email
      ).as(User.simple.singleOpt)
    }
  }
  def findAll: Seq[User] = {
    DB.withConnection { implicit c =>
      SQL("select * from user").as(User.simple *)
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
        ).as(User.simple.singleOpt)
    }
  }

  def create(user: User): User = {
    DB.withConnection { implicit c =>
      SQL(
        """
          insert into user (
            email, name, password, created, modified
          )
          values (
            {email}, {name}, {password}, {created}, {modified}
          )
        """
      ).on(
          'email -> user.email,
          'name -> user.name,
          'password -> user.password,
          'created -> user.created,
          'modified -> user.modified
        ).executeUpdate()

      user
    }
  }
}
