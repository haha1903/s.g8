package $package$

import org.scalatra._

class $name;format="Camel"$Servlet extends $name;format="Camel"$Stack {

  get("/") {
  	contentType = null
    <html>
      <body>
        <h1>Hello, world!</h1>
        <a href="/swagger">Hello Swagger</a>
      </body>
    </html>
  }
}
