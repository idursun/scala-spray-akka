package domain

import akka.actor._
import akka.persistence._
import common.Acknowledge

object AggregateRoot {

  trait State
  case object Uninitialized extends State
  case object Removed extends State

  trait Event

  trait Command
  case object Remove extends Command
  case object GetState extends Command
  case object KillAggregate extends Command

  val eventsPerSnapshot = 10
}

trait AggregateRoot extends PersistentActor with ActorLogging {
  import AggregateRoot._

  override def persistenceId:String
  protected var state: AggregateRoot.State = Uninitialized

  private var eventsSinceLastSnapshot: Int = 0

  protected def afterEventPersisted(evt: Event):Unit = {
    eventsSinceLastSnapshot += 1
    if (eventsSinceLastSnapshot > eventsPerSnapshot) {
      log.debug("{} events reached, saving snapshot", eventsPerSnapshot)
      saveSnapshot(state)
      eventsSinceLastSnapshot = 0
    }
    updateState(evt)
    respond()
    publish(evt)
  }

  protected def respond(): Unit = {
    sender() ! state
    context.parent ! Acknowledge(persistenceId)
  }

  private def publish(event: Event) =
    context.system.eventStream.publish(event)


  def updateState(evt: Event): Unit

  def receiveRecover:Receive = {
    case evt: Event =>
      eventsSinceLastSnapshot += 1
      updateState(evt)
    case SnapshotOffer(metadata, state: State) =>
      restoreFromSnapshot(metadata, state)
      log.debug("recovering aggregate from snapshot")
  }

  protected def restoreFromSnapshot(metadata: SnapshotMetadata, state: AggregateRoot.State)

}
