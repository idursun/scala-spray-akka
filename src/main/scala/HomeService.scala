import spray.http._
import spray.routing._
import spray.json.DefaultJsonProtocol
import spray.httpx.SprayJsonSupport._

case class Person(name: String, age: Int)

trait HomeService extends HttpService { 
    
    object JsonImplicits extends DefaultJsonProtocol {
        implicit val PersonFormatter = jsonFormat2(Person)    
    }
    
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
        }
    }    
}