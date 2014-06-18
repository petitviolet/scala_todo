package controllers

import play.api.mvc._
import org.pac4j.play.scala.ScalaController
import org.pac4j.oauth.profile.twitter.TwitterProfile
import models.TwitterAuthConfig
import twitter4j.auth.AccessToken
import twitter4j.TwitterFactory
import scala.collection.JavaConversions._

/**
 * Created by Komurasaki on 2014/06/17.
 */
class TwitterController extends ScalaController with TwitterAuthConfig {
  lazy val twitter = {
    val tw = TwitterFactory.getSingleton
    tw.setOAuthConsumer(consumer.key, consumer.secret)
    tw
  }

  def index = Action { implicit request =>
    val newSession = getOrCreateSessionId(request)
    val content = Option(getUserProfile(request)).fold(getRedirectAction(request, newSession, "TwitterClient", "/").getLocation) { p =>
      val tp = p.asInstanceOf[TwitterProfile]
      val ac = new AccessToken(tp.getAccessToken, tp.getAccessSecret)

      twitter.setOAuthAccessToken(ac)
      twitter.getUserTimeline.map(_.getText).mkString("\n")
    }
    Ok(content)
  }
}
