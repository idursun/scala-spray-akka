package domain
import storage.Store

trait EventStore {
    val backend: Store[Event]
}