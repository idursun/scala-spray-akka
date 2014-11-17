import domain.TopicAggregate.Topic
import route.{JsonImplicits, Person, TopicRoute}
import spray.util._
import spray.http._
import spray.http.StatusCodes._
import spray.http.MediaTypes._
import spray.httpx.SprayJsonSupport._
import spray.testkit.ScalatestRouteTest
import org.scalatest.FreeSpec
import org.scalatest.Matchers._

class TopicRouteSpec extends FreeSpec with ScalatestRouteTest with TopicRoute {
    
    import JsonImplicits._
    
    def actorRefFactory = system
 
    "Topic Service" - {
        "should return topic" in {
            Get("/topic/first") ~> topicRoute ~> check {
                status === OK
                contentType === `application/json`
                val topic = responseAs[Topic]
                topic.title === "first"
            }
        }

        "should create topic when entry is added" in {
            Post("/topic/first", Entry("second", 1)) ~> sealRoute(topicRoute) ~> check {
                status == Created
                responseAs[String] === "2"
            }
        }
     
    }
    
}