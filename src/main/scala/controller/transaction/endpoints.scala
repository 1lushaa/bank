package controller.transaction

import domain.error.TransactionError.TransactionsInternalError
import domain.error.given
import domain.transaction.CreateTransaction.CreateTransferBetweenAccounts
import domain.transaction.Transaction.TransferBetweenAccounts
import domain.transaction.TransactionId
import sttp.tapir.*
import sttp.tapir.generic.auto.*
import sttp.tapir.json.circe.jsonBody

object endpoints {

  val getTransferBetweenAccountsById
    : PublicEndpoint[TransactionId, TransactionsInternalError, Option[
      TransferBetweenAccounts
    ], Any] =
    endpoint.get
      .in("transfer_between_accounts" / path[TransactionId])
      .errorOut(jsonBody[TransactionsInternalError])
      .out(jsonBody[Option[TransferBetweenAccounts]])

  val createTransferBetweenAccounts: PublicEndpoint[
    CreateTransferBetweenAccounts,
    TransactionsInternalError,
    TransferBetweenAccounts,
    Any
  ] = endpoint.post
    .in("transfer_between_accounts" / jsonBody[CreateTransferBetweenAccounts])
    .errorOut(jsonBody[TransactionsInternalError])
    .out(jsonBody[TransferBetweenAccounts])

}
