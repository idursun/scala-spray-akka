package service

import akka.actor._
import domain.AggregateRoot
import scala.collection.immutable.{Nil, Seq, Set}

object AggregateManager {
  trait Command

  val maxChildren = 40
  val childrenToKillAtOnce = 20

  case class PendingCommand(sender: ActorRef, targetProcessorId: String, command: AggregateRoot.Command)
}

trait AggregateManager extends Actor with ActorLogging {

  import AggregateRoot._
  import AggregateManager._

  private var childrenBeingTerminated: Set[ActorRef] = Set.empty
  private var pendingCommands: Seq[PendingCommand] = Nil

  def processCommand: Receive

  private def defaultProcessCommand: Receive = {
    case Terminated(actor) =>
      childrenBeingTerminated = childrenBeingTerminated filterNot (_ == actor)
      val (commandsForChild, remainingCommands) = pendingCommands partition (_.targetProcessorId == actor.path.name)
      pendingCommands = remainingCommands
      log.debug("Child termination finished. Applying {} cached commands.", commandsForChild.size)
      for (PendingCommand(commandSender, targetProcessorId, command) <- commandsForChild) {
        val child = findOrCreate(targetProcessorId)
        child.tell(command, commandSender)
      }
  }

  private def findOrCreate(processorId: String):ActorRef = context.child(processorId) getOrElse create(processorId)

  private def create(processorId: String):ActorRef = {
    val agg = context.actorOf(aggregateProps(processorId), processorId)
    context watch agg
  }

  def aggregateProps(id: String): Props

  override def receive: Receive = processCommand orElse defaultProcessCommand


}
