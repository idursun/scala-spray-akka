package domain

import akka.actor.Props
import akka.persistence.SnapshotMetadata
import domain.AggregateRoot.{Uninitialized, KillAggregate, Event}

object TopicAggregate {
  import AggregateRoot._

  case class EntryAdded(id: Int, userId: String, content: String) extends Event
  case class EntryEdited(id: Int, content: String) extends Event
  case class EntryDeleted(id: Int) extends Event

  case class Entry(id: Int, userId: String, content: String)
  case class Topic(title: String, entries: List[Entry] ) extends State

  case class AddEntry(id: Int, userId: String, content: String) extends Command
  case class DeleteEntry(id: Int) extends Command

  def props(id: String):Props = Props(new TopicAggregate(id))
}

class TopicAggregate(title: String) extends AggregateRoot {

  import TopicAggregate._

  override def persistenceId = title

  override def updateState(evt: Event): Unit = evt match {
    case e: EntryAdded => state match  {
      case Uninitialized =>
        context.become(created)
        state = Topic(title, entries = List())
      case s: Topic =>
        state = s.copy(entries = Entry(e.id, e.userId, e.content) :: s.entries)
    }
    case EntryDeleted(entryId) => state match {
      case s: Topic =>
        val newState = s.copy(entries = s.entries.filterNot(_.id == entryId ) )
        if (newState.entries.isEmpty)
          context.become(removed)
        state = newState
      case _ =>
    }
  }

  val initial: Receive = {
    case AddEntry(id, userId, content) =>
      context.become(created)
      state = Topic(title, entries = List())
      persist(EntryAdded(id, userId, content))(afterEventPersisted)
  }

  val removed: Receive = {
    case KillAggregate => context.stop(self)
  }

  val created: Receive = {
    case AddEntry(id, userId, content) =>
    case DeleteEntry(id) =>
      persist(EntryDeleted(id))(afterEventPersisted)
  }

  def receiveCommand:Receive = initial

  override def restoreFromSnapshot(metadata: SnapshotMetadata, state: AggregateRoot.State) =  {
    this.state = state
    state match {
      case AggregateRoot.Uninitialized => context become initial
      case AggregateRoot.Removed => context become removed
    }

  }

}
