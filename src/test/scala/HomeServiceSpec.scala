import spray.util._
import spray.http._
import spray.http.StatusCodes._
import spray.http.MediaTypes._
import spray.httpx.SprayJsonSupport._
import spray.testkit.ScalatestRouteTest
import org.scalatest.FreeSpec
import org.scalatest.Matchers._

class HomeServiceSpec extends FreeSpec with ScalatestRouteTest with HomeService with InMemoryPersistenceComponent {
    
    import JsonImplicits._
    
    def actorRefFactory = system
 
    "Home Service" - {
        "should return person data" in {
            Get("/person/1") ~> myRoute ~> check {
                status === OK
                contentType === `application/json`
                val person = responseAs[Person]
                person.age should be > 0
                person.name should not be (null)
            }
        }

        "should return total count of persons" in {
            PersonStore.add(Person("first",1))

            Post("/person", Person("second", 1)) ~> sealRoute(myRoute) ~> check {
                status == Created
                responseAs[String] === "2"
            }
        }
     
    }
    
}