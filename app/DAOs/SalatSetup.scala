import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.conversions.scala.RegisterJodaTimeConversionHelpers
import com.novus.salat.{Context, StringTypeHintStrategy, TypeHintFrequency}

package object DAOs {
  implicit val context = {
    RegisterJodaTimeConversionHelpers()

    val context = new Context {
      val name = "global"
      override val typeHintStrategy = StringTypeHintStrategy(when = TypeHintFrequency.WhenNecessary, typeHint = "_t")
    }

    context.registerGlobalKeyOverride(remapThis = "id", toThisInstead = "_id")
    context
  }

  def defineCollection(name: String) = {
    MongoClient(MongoClientURI(databaseUri))(databaseName)(name)
  }

  def databaseName = {
    sys.env.getOrElse("DATABASE_NAME", "vielen-games-api")
  }

  def databaseUri = {
    sys.env.getOrElse("DATABASE_URI", "mongodb://localhost:27017/")
  }
}