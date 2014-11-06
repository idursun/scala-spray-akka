package domain

trait Event {
  val id: java.util.UUID = java.util.UUID.randomUUID()
  val ticks: Int = 0
}

case class RegisterProject(name: String, description: String) extends Event
case class DeleteProject(code: String) extends Event


case class Person(name: String, age: Int)