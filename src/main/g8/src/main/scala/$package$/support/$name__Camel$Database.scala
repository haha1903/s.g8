package $package$.support

import scala.concurrent.Await
import scala.concurrent.duration.Duration

trait $name;format="Camel"$Database extends DatabaseSupport {
  val payDb = Database.forConfig("$name;format="lower"$")

  def payRun[R](a: DBIOAction[R, NoStream, Nothing]): R = Await.result(payDb.run(a), Duration.Inf)
}
