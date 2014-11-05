import spray.http._
import spray.routing._
import spray.httpx.SprayJsonSupport._
import spray.json.DefaultJsonProtocol

case class Person(name: String, age: Int)

trait HomeService extends HttpService { 
    
    import DefaultJsonProtocol._
    implicit val PersonFormatter = jsonFormat2(Person)
    
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
            
        }
    }    
}