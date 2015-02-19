import com.mongodb.casbah.commons.conversions.scala.RegisterJodaTimeConversionHelpers
import com.novus.salat.{Context, StringTypeHintStrategy, TypeHintFrequency}

package object DAOs {
  val DatabaseName = "vielen-games"

  implicit val context = {
    RegisterJodaTimeConversionHelpers()

    val context = new Context {
      val name = "global"
      override val typeHintStrategy = StringTypeHintStrategy(when = TypeHintFrequency.WhenNecessary, typeHint = "_t")
    }

    context.registerGlobalKeyOverride(remapThis = "id", toThisInstead = "_id")
    context
  }
}