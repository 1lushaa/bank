package controller.account

import domain.account.*
import domain.account.Account.PaymentAccount
import domain.account.CreateAccount.CreatePaymentAccount
import domain.error.AccountError.*
import domain.error.{*, given}
import sttp.tapir.*
import sttp.tapir.generic.auto.*
import sttp.tapir.json.circe.jsonBody

object endpoints {

  val getPaymentAccountById
    : PublicEndpoint[PaymentAccountId, AccountsInternalError, Option[PaymentAccount], Any] =
    endpoint.get
      .in("payment_accounts" / path[PaymentAccountId])
      .errorOut(jsonBody[AccountsInternalError])
      .out(jsonBody[Option[PaymentAccount]])

  val getPaymentAccountByNumber
    : PublicEndpoint[AccountNumber, AccountsInternalError, Option[PaymentAccount], Any] =
    endpoint.get
      .in("payment_accounts" / path[AccountNumber])
      .errorOut(jsonBody[AccountsInternalError])
      .out(jsonBody[Option[PaymentAccount]])

  val createPaymentAccount
    : PublicEndpoint[CreatePaymentAccount, AccountError, PaymentAccount, Any] =
    endpoint.post
      .in("payment_accounts" / jsonBody[CreatePaymentAccount])
      .errorOut(jsonBody[AccountError])
      .out(jsonBody[PaymentAccount])

}
