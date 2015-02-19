import com.mongodb.casbah.{MongoClient}
import org.specs2.mutable.Specification
import org.specs2.specification.Step

trait InteractorSpec extends Specification {
  override def is = Step { MongoClient().dropDatabase(DAOs.DatabaseName) } ^ super.is
}