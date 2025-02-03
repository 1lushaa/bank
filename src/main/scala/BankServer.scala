import cats.effect.{IO, IOApp}
import com.comcast.ip4s.{Host, Port}
import config.{dbConfig, serverConfig}
import controller.account.AccountController
import controller.client.ClientController
import controller.transaction.TransactionController
import domain.client.NaturalPersonId
import doobie.implicits.{toConnectionIOOps, toSqlInterpolator}
import doobie.util.transactor.Transactor
import org.http4s.HttpApp
import org.http4s.ember.server.EmberServerBuilder
import service.{AccountService, ClientService, TransactionService}
import sttp.tapir.server.http4s.Http4sServerInterpreter

object BankServer extends IOApp.Simple {

  private val transactor = Transactor.fromDriverManager[IO](
    driver = dbConfig.dbDriver,
    url = s"jdbc:postgresql://${dbConfig.dbHost}:${dbConfig.dbPort}/${dbConfig.dbName}",
    logHandler = None,
    user = dbConfig.dbUserName,
    password = dbConfig.dbUserPassword
  )

  private val clientService = ClientService.make(transactor)
  private val clientController = ClientController.make(clientService)

  private val accountService = AccountService.make(transactor)
  private val accountController = AccountController.make(accountService)

  private val transactionService = TransactionService.make(transactor)
  private val transactionController = TransactionController.make(transactionService)

  private val httpApp: HttpApp[IO] = Http4sServerInterpreter[IO]()
    .toRoutes(
      clientController.getAllEndpoints ++
        accountController.getAllEndpoints ++
        transactionController.getAllEndpoints
    )
    .orNotFound

  private def checkDBConnection(): IO[Either[Throwable, Int]] =
    sql"select 1".query[Int].unique.transact(transactor).attempt

  override def run: IO[Unit] =
    checkDBConnection().flatMap {
      case Left(error) => IO.println(s"Unable to connect to DB: ${error.getMessage}.")
      case Right(_) =>
        clientService.findNaturalPersonById(NaturalPersonId(1)).flatMap(user => IO(println(s"Result: $user")))
        EmberServerBuilder
        .default[IO]
        .withHost(Host.fromString(serverConfig.serverHost).get)
        .withPort(Port.fromString(serverConfig.serverPort).get)
        .withHttpApp(httpApp)
        .build
        .useForever
    }
}
