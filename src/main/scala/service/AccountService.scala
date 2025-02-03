package service

import cats.effect.IO
import cats.implicits.{catsSyntaxEither, catsSyntaxEitherId}
import dao.AccountSql
import domain.account
import domain.account.*
import domain.account.Account.PaymentAccount
import domain.account.CreateAccount.CreatePaymentAccount
import domain.error.AccountError
import domain.error.AccountError.AccountsInternalError
import doobie.Transactor
import doobie.implicits.toConnectionIOOps

trait AccountService {

  def getPaymentAccountById(
    id: PaymentAccountId
  ): IO[Either[AccountsInternalError, Option[PaymentAccount]]]

  def getPaymentAccountByNumber(
    number: AccountNumber
  ): IO[Either[AccountsInternalError, Option[PaymentAccount]]]

  def createPaymentAccount(account: CreatePaymentAccount): IO[Either[AccountError, PaymentAccount]]

}

object AccountService {

  private final class AccountServiceImpl(accountSql: AccountSql, transactor: Transactor[IO])
      extends AccountService {

    override def getPaymentAccountById(
      id: PaymentAccountId
    ): IO[Either[AccountsInternalError, Option[PaymentAccount]]] =
      accountSql
        .findPaymentAccountById(id)
        .transact(transactor)
        .attempt
        .map(_.leftMap(AccountsInternalError.apply))

    override def getPaymentAccountByNumber(
      number: AccountNumber
    ): IO[Either[AccountsInternalError, Option[PaymentAccount]]] =
      accountSql
        .findPaymentAccountByNumber(number)
        .transact(transactor)
        .attempt
        .map(_.leftMap(AccountsInternalError.apply))

    override def createPaymentAccount(
      account: CreatePaymentAccount
    ): IO[Either[AccountError, PaymentAccount]] =
      accountSql.createPaymentAccount(account).transact(transactor).attempt.map {
        case Left(error)           => AccountsInternalError(error).asLeft
        case Right(Left(error))    => error.asLeft
        case Right(Right(account)) => account.asRight
      }

  }

  def make(transactor: Transactor[IO]): AccountService =
    new AccountServiceImpl(AccountSql.make, transactor)

}
