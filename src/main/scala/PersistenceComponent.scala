trait PersistenceComponent {
  val PersonStore: PersonStore

  trait Store[T] {
    def add(item: T)
    def all: List[T]
    def count: Int
  }

  trait PersonStore extends Store[Person]
}

trait InMemoryPersistenceComponent extends PersistenceComponent {

  trait InMemoryStore[T] extends Store[T] {
    protected[this] val store = scala.collection.mutable.ListBuffer[T]()
    def add(item: T) = store += item
    def all: List[T] = store.toList
    def count: Int = store.length
  }

  val PersonStore: PersonStore = new PersonStore with InMemoryStore[Person]
}
