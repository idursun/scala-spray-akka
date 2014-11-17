package service

import akka.actor.Props
import domain.TopicAggregate

object TopicAggregateManager {

}

class TopicAggregateManager extends AggregateManager {
  override def processCommand: Receive = ???

  override def aggregateProps(id: String): Props = TopicAggregate.props(id)
}
