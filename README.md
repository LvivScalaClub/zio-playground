# zio-playground

## Social network
### Desired Functionality
  - User Registration                       (Viktor)
  - User Authentication & Authorization     (Danylo)
  
https://http4s.org/v0.19/auth/ (`Authorization Header` section)

Suggest using `Basic Authorization` for simplicity.

https://github.com/http4s/http4s/blob/698e7a2f2aeed71e13439f24f36772ec4901e7d6/server/src/main/scala/org/http4s/server/middleware/authentication/BasicAuth.scala
https://github.com/http4s/http4s/blob/master/server/src/test/scala/org/http4s/server/middleware/authentication/AuthenticationSpec.scala

1. Parse `Authorization` header 
2. Fetch user for given username and password
3. Grand or deny access

  - Friends: Add/Remove/List                (Vadym)
  - Feed: posts, likes, comments            (Andriy)

### High-level components
 - Http-Service: Http4s, guardrail to generate server/client boilerplate
 - Storage: PostgreSql + Doobie, consider Cassandra for posts, likes, comments
