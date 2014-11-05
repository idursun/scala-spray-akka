import org.scalatest.FreeSpec
import spray.testkit.ScalatestRouteTest
import org.scalatest.Matchers
import spray.http.StatusCodes._

class HomeServiceSpec extends FreeSpec with ScalatestRouteTest with HomeService {
    
    def actorRefFactory = system
 
    "Home Service" - {
        "should return person data " in {
            Get("/person/1") ~> myRoute ~> check {
                status === OK
            }
        }
    }
    
}