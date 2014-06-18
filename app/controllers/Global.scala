package controllers

/**
 * Created by Komurasaki on 2014/06/17.
 */

import play.api.GlobalSettings
import play.api.mvc.{ Results, SimpleResult, RequestHeader }
import play.api.mvc._
import scala.concurrent.Future
import org.pac4j.oauth.client.TwitterClient
import org.pac4j.core.client.Clients
import org.pac4j.play.Config
import models.TwitterAuthConfig

object Global extends GlobalSettings with TwitterAuthConfig {

  override def onError(request: RequestHeader, ex: Throwable) = {
    Future.successful(Results.InternalServerError)
  }

  override def onStart(app: play.api.Application): Unit = {
    val ti = consumer
    val tc = new TwitterClient(ti.key, ti.secret)
    val cl = new Clients(callback, tc)
    Config.setClients(cl)
  }
}