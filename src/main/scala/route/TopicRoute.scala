package route

import domain.TopicAggregate.{Topic, Entry}
import spray.http._
import spray.httpx.SprayJsonSupport._
import spray.json.DefaultJsonProtocol
import spray.routing._

object JsonImplicits extends DefaultJsonProtocol {
  implicit val EntryFormatter = jsonFormat3(Entry)
  implicit val TopicFormatter = jsonFormat2(Topic)
}

trait TopicRoute extends HttpService  {

    import route.JsonImplicits._

    val topicRoute = {
        path("topic" / IntNumber) { id =>
            get {
                complete {
                    <p>info for project <strong>{id}</strong></p>
                }
            }
        } ~
        path("topic") {
          (post & entity(as[Entry])) { person =>
            complete{
              HttpResponse(StatusCodes.Created, HttpEntity("1"))
            }
          }
        }
    }    
}