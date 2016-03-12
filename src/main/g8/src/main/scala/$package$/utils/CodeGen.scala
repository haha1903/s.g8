package $package$.utils

import com.mb.support.{ConfigSupport, DatabaseSupport}
import slick.driver.MySQLDriver

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object CodeGen extends App with DatabaseSupport with ConfigSupport {

  import slick.codegen.SourceCodeGenerator

  import scala.concurrent.ExecutionContext.Implicits.global

  import conf._

  val db = Database.forConfig("gen")
  val output = args(0)
  val pkg = getString("gen.pkg")
  // fetch data model
  val modelAction = MySQLDriver.createModel(Some(MySQLDriver.defaultTables))
  // you can filter specific tables here
  val modelFuture = db.run(modelAction)
  // customize code generator
  val codegenFuture = modelFuture.map(model => new SourceCodeGenerator(model) {

    override val ddlEnabled = false

    override def docWithCode(doc: String, code: String): String = code

    override def Table = new Table(_) {

      override def hlistEnabled = false

      override def compoundValue(values: Seq[String]): String = if (values.size == 1) values.head else s"""(${values.mkString(", ")})"""

      override def PlainSqlMapper = new PlainSqlMapper {
        override def enabled = false
      }

      override def TableValue = new TableValue {
        override def code = s"lazy val $name = TableQuery[${TableClass.name}]"
      }

      override def TableClass = new TableClass {
        override def optionEnabled = false
      }

      override def Column = new Column(_) {
        override def code = s"""val $name = column[$actualType]("${model.name}"${options.map(", " + _).mkString("")})"""
      }

      override def code: Seq[String] = definitions.flatMap(_.getEnabled).filterNot(d => d.isInstanceOf[EntityType]).map(_.docWithCode)
    }

    def entities = {
      tables.map(_.EntityType.code).mkString("\n\n")
    }

    override def packageCode(profile: String, pkg: String, container: String, parentType: Option[String]): String = {
      s"""
package ${pkg}

${entities}

object ${container} extends {
  val profile = $profile
} with ${container}
trait ${container}${parentType.map(t => s" extends $t").getOrElse("")} {
  val profile: slick.driver.JdbcProfile
  import profile.api._
  ${indent(code)}
}
      """.trim()
    }
  })
  val codegen = Await.result(codegenFuture, Duration.Inf)
  codegen.writeToFile(
    "slick.driver.MySQLDriver", output, pkg, "Tables", "Tables.scala"
  )
}
