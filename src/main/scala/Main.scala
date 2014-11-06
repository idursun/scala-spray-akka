import akka.actor._
import akka.io.IO
import spray.http._
import spray.can.Http
import storage.InMemoryPersistenceComponent
import services.HomeService

class HomeServiceActor extends Actor with HomeService with InMemoryPersistenceComponent with ActorLogging {
    
    def actorRefFactory = context
    
    def receive = runRoute(myRoute)
}

object Program extends App {
    
    implicit val system = ActorSystem()
    
    val handler = system.actorOf(Props[HomeServiceActor])
    
    IO(Http) ! Http.Bind(handler, interface="::0", port=8080)
}