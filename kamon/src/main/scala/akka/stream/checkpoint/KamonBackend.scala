package akka.stream.checkpoint

object KamonBackend {

  implicit val instance: CheckpointBackend = new CheckpointBackend {
    override def createRepository(name: String): CheckpointRepository = KamonCheckpointRepository(name)
  }
}
