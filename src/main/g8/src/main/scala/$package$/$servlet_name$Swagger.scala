package $package$

import org.scalatra._
import org.scalatra.swagger.annotations._

case class User(@ApiModelProperty(description = "User Id") id: Long,
                @ApiModelProperty(description = "User Name") name: String)

class $servlet_name$Swagger extends $name;format="Camel"$Stack {
  post("/user", api[List[User], Unit]("List users")) {
    List(
      User(1, "haha"),
      User(2, "hehe")
    )
  }
}
