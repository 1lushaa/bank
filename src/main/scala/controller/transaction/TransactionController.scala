package controller.transaction

import cats.effect.IO
import service.TransactionService
import sttp.tapir.server.ServerEndpoint

trait TransactionController {

  def getTransferBetweenAccountsById: ServerEndpoint[Any, IO]

  def createTransferBetweenAccounts: ServerEndpoint[Any, IO]

  def getAllEndpoints: List[ServerEndpoint[Any, IO]]

}

object TransactionController {

  private final class TransactionControllerImpl(service: TransactionService)
      extends TransactionController {

    override def getTransferBetweenAccountsById: ServerEndpoint[Any, IO] =
      endpoints.getTransferBetweenAccountsById.serverLogic(id =>
        service.getTransferBetweenAccountsById(id)
      )

    override def createTransferBetweenAccounts: ServerEndpoint[Any, IO] =
      endpoints.createTransferBetweenAccounts.serverLogic(transfer =>
        service.createTransferBetweenAccounts(transfer)
      )

    override def getAllEndpoints: List[ServerEndpoint[Any, IO]] =
      List(getTransferBetweenAccountsById, createTransferBetweenAccounts)

  }

  def make(service: TransactionService): TransactionController =
    new TransactionControllerImpl(service)

}
