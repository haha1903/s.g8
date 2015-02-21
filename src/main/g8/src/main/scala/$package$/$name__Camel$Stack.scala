package $package$

import org.scalatra._
import org.scalatra.swagger._
import org.scalatra.swagger.reflect.Reflector
import org.scalatra.swagger.runtime.annotations.ApiModel
import javax.servlet.http.HttpServletRequest
import org.json4s.DefaultFormats
import org.scalatra.json.JacksonJsonSupport

trait $name;format="Camel"$Stack extends ScalatraServlet with JacksonJsonSupport with SwaggerSupport {
  protected def applicationDescription = "$name;format="Camel"$"
  implicit val swagger = ResourceSwagger
  implicit val jsonFormats = DefaultFormats

  before() {
    contentType = formats("json")
  }

  def api[R: Manifest, P: Manifest](name: String) = operation(apiOperation[R](name)
    summary name
    parameter (bodyParam[P]))

  // add ApiModel parent properties to Model
  override protected def registerModel(model: Model) = {
    val mergeModel = try {
      val clz = Class.forName(model.qualifiedName.get)
      val klass = Reflector.scalaTypeOf(clz)
      val apiModel = Option(klass.erasure.getAnnotation(classOf[ApiModel]))
      apiModel match {
        case Some(am) =>
          val parent = am.parent()
          if (parent != classOf[Void]) {
            val pModel = Swagger.modelToSwagger(Reflector.scalaTypeOf(parent)).get
            val props = model.properties
            val pProps = pModel.properties.filterNot(p => props.map(_._1).contains(p._1))
            model.copy(properties = props ::: pProps)
          } else {
            model
          }
        case None => model
      }
    } catch {
      case e: Exception => model
    }
    super.registerModel(mergeModel)
  }

  notFound {
    // remove content type in case it was set through an action
    contentType = null
    serveStaticResource() getOrElse resourceNotFound()
  }
}
