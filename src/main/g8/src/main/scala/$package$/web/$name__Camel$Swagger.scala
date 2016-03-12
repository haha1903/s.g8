package $package$.web

import org.scalatra._
import $package$.support._

case class User(@p(description = "User Id") id: Long,
                @p(description = "User Name") name: String)

class $name;format="Camel"$Swagger extends $name;format="Camel"$Stack {
  post("/user", api[List[User], Unit]("List users")) {
    List(
      User(1, "haha"),
      User(2, "hehe")
    )
  }
}
