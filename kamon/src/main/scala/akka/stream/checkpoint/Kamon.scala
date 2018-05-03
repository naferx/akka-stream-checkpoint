package akka.stream.checkpoint

object Kamon {

  implicit val factory: CheckpointRepositoryFactory = new CheckpointRepositoryFactory {
    override def createRepository(name: String): CheckpointRepository = KamonCheckpointRepository(name)
  }
}
