import akka.actor._
import akka.io.IO
import route.TopicRoute
import spray.http._
import spray.can.Http

class ServiceActor extends Actor with TopicRoute with  ActorLogging {
    
    def actorRefFactory = context
    
    def receive = runRoute(topicRoute)
}

object Program extends App {
    
    implicit val system = ActorSystem()
    
    val handler = system.actorOf(Props[ServiceActor])
    
    IO(Http) ! Http.Bind(handler, interface="::0", port=8080)
}