package services

import spray.http._
import spray.routing._
import spray.json.DefaultJsonProtocol
import spray.httpx.SprayJsonSupport._
import storage.PersistenceComponent
import domain.Person

object JsonImplicits extends DefaultJsonProtocol {
  implicit val PersonFormatter = jsonFormat2(Person)
}

trait HomeService extends HttpService with PersistenceComponent {

    import JsonImplicits._

    val myRoute = {
        path("ping") {
            get {
                complete {
                    <h1>Hello</h1>
                }
            }
        } ~ 
        path("projects" / IntNumber) { id =>
            compressResponse() {
                get {
                    complete {
                        <p>info for project <strong>{id}</strong></p>
                    }
                }
            }
        } ~
        path("person" / IntNumber) { id =>
            get {
                complete {
                    Person("person " + id, 1 + id * 4)
                }
            }
        } ~
        path("person") {
          entity(as[Person]) { person =>
            post {
              complete{
                PersonStore.add(person)
                HttpResponse(StatusCodes.Created, HttpEntity(PersonStore.count.toString))
              }
            }
          }
        }
    }    
}