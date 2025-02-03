package controller.account

import cats.effect.IO
import service.AccountService
import sttp.tapir.server.ServerEndpoint

trait AccountController {

  def getPaymentAccountById: ServerEndpoint[Any, IO]

  def getPaymentAccountByNumber: ServerEndpoint[Any, IO]

  def createPaymentAccount: ServerEndpoint[Any, IO]

  def getAllEndpoints: List[ServerEndpoint[Any, IO]]

}

object AccountController {

  private final class AccountControllerImpl(service: AccountService) extends AccountController {

    override def getPaymentAccountById: ServerEndpoint[Any, IO] =
      endpoints.getPaymentAccountById.serverLogic(id => service.getPaymentAccountById(id))

    override def getPaymentAccountByNumber: ServerEndpoint[Any, IO] =
      endpoints.getPaymentAccountByNumber.serverLogic(number =>
        service.getPaymentAccountByNumber(number)
      )

    override def createPaymentAccount: ServerEndpoint[Any, IO] =
      endpoints.createPaymentAccount.serverLogic(account => service.createPaymentAccount(account))

    override def getAllEndpoints: List[ServerEndpoint[Any, IO]] =
      List(
        getPaymentAccountById,
        getPaymentAccountByNumber,
        createPaymentAccount
      )

  }

  def make(service: AccountService): AccountController = new AccountControllerImpl(service)

}
