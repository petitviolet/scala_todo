package controllers

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import models._
import play.api._
import org.joda.time.DateTime
import models.AnormDateExtension._

/**
 * Created by Komurasaki on 2014/06/18.
 */
object UserController extends Controller with Secured {
  // ログインフォーム
  val loginForm = Form(
    tuple(
      "email" -> nonEmptyText,
      "password" -> nonEmptyText
    ) verifying ("ログインに失敗しました", result => result match {
      case (email, password) => User.authenticate(email, password).isDefined
    })
  )

  // トップページ
//  def show = Action {
//    Ok(views.html.test("Oops!!!"))
//  }}

  def show = IsAuthenticated {email => _ => {
    val user: Option[User] = User.findByEmail(email)
    Logger.debug("UserController.show =>" + user + ", " + email)
    user.map {
      user => Ok(views.html.user(user))
    } .getOrElse {
      Redirect(routes.UserController.login())
    }
  }}


  // ログインページ
  def login = Action { implicit request =>
    Ok(views.html.login(loginForm))
  }

  // ユーザ認証
  def authenticate = Action { implicit request => {
    Logger.debug("authenticate")
    loginForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.login(formWithErrors)),
      user => Redirect(routes.UserController.show)
        .withSession(session +
          ("email" -> user._1) +
          ("validSession" -> "1") +
          ("startSession" -> new DateTime().toString())
        )
    )
  }}

  // ログアウト
  def logout = Action {
    Redirect(routes.UserController.login).withNewSession.flashing(
      "success" -> "ログアウトしました"
    )
  }

  // 登録フォーム
  val signupForm = Form(
    tuple(
      "email" -> nonEmptyText.verifying(
        "このメールアドレスは既に登録されています",
        email => User.findByEmail(email).isEmpty
      ),
      "name" -> nonEmptyText,
      "password" -> tuple(
        "main" -> nonEmptyText,
        "confirm" -> nonEmptyText
      ).verifying(
          "パスワードが一致しません",
          password => password._1 == password._2
        )
    )
  )

  // ユーザ登録ページ
  def signup = Action {
    Ok(views.html.signup(signupForm))
  }

  // ユーザ登録
  def register = Action { implicit request =>
    signupForm.bindFromRequest.fold(
      errors => BadRequest(views.html.signup(errors)),
      form => {
        val timeStamp = new DateTime()
        Logger.debug("timestamp =>" + timeStamp)
        val user: Option[User] =
          User.create(form._1, form._2, form._3._1,
            timeStamp, timeStamp)
        user.map {
          user => Ok(views.html.user(user))
        }.getOrElse {
          Redirect(routes.UserController.signup())
        }
      }
    )
  }
}
