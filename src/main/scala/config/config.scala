package config

case object dbConfig {
  val dbDriver = "org.postgresql.Driver"
  val dbHost = "localhost"
  val dbPort = "5432"
  val dbName = "my_bank"
  val dbUserName = "postgres"
  val dbUserPassword = "abacaba52"
}

case object serverConfig {
  val serverPort = "8080"
  val serverHost = "localhost"
}