import java.net.HttpURLConnection

import DAOs.UserDAO
import com.restfb.WebRequestor.Response
import com.restfb.{DefaultJsonMapper, DefaultWebRequestor, DefaultFacebookClient}
import interactors.LoginInteractor

class LoginInteractorSpec extends InteractorSpec {
  val fbclient = new DefaultFacebookClient("test-access-token",
    new DefaultWebRequestor() {
      override def executeGet(url: String) = {
        new Response(HttpURLConnection.HTTP_OK, "{'id':'provider_id','name':'Test person'}")
      }
    }, new DefaultJsonMapper())

  val interactor = new LoginInteractor(fbclient)

  "Login interactor" should {

    "register new user if not exist" in {
      val user = interactor.call

      user.providerId must beEqualTo("provider_id")
      user.name must beEqualTo("Test person")
      user.authenticateToken must not beEmpty
    }

    "only returns user if already exist" in {
      val notRegisteredUser = interactor.call
      val alreadyRegisteredUser = interactor.call

      UserDAO.count() must beEqualTo(1)
      notRegisteredUser must beEqualTo(alreadyRegisteredUser)
    }

  }
}
