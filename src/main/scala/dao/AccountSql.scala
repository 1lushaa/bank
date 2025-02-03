package dao

import cats.syntax.applicative.given
import cats.syntax.either.given
import domain.account.*
import domain.account.Account.*
import domain.account.CreateAccount.CreatePaymentAccount
import domain.error.AccountError.PaymentAccountAlreadyExists
import doobie.implicits.toSqlInterpolator
import doobie.util.update.Update0
import doobie.{ConnectionIO, Query0}

import java.time.LocalDate

trait AccountSql {

  def findPaymentAccountById(id: PaymentAccountId): ConnectionIO[Option[PaymentAccount]]

  def findPaymentAccountByNumber(number: AccountNumber): ConnectionIO[Option[PaymentAccount]]

  def createPaymentAccount(
    account: CreatePaymentAccount
  ): ConnectionIO[Either[PaymentAccountAlreadyExists, PaymentAccount]]

}

object AccountSql {

  object sqls {

    def findPaymentAccountByIdSql(id: PaymentAccountId): Query0[PaymentAccount] =
      sql"select * from payment_accounts where id=${id.value}".query[PaymentAccount]

    def findPaymentAccountByNumberSql(number: AccountNumber): Query0[PaymentAccount] =
      sql"select * from payment_accounts where account_number=${number.value}".query[PaymentAccount]

    def createPaymentAccountSql(account: CreatePaymentAccount): Update0 =
      sql"""
           insert into payment_accounts
           (account_number, account_bic, account_swift, currency, creation_date)
           value
           (${account.number.value}, ${account.bic.value}, ${account.swift.value}, 
           ${account.currency.toString}, ${account.creationDate.toString})
         """.update

  }

  private final class AccountSqlImpl extends AccountSql {

    import sqls.*

    override def findPaymentAccountById(
      id: PaymentAccountId
    ): ConnectionIO[Option[PaymentAccount]] = findPaymentAccountByIdSql(id).option

    override def findPaymentAccountByNumber(
      number: AccountNumber
    ): ConnectionIO[Option[PaymentAccount]] = findPaymentAccountByNumberSql(number).option

    override def createPaymentAccount(
      account: CreatePaymentAccount
    ): ConnectionIO[Either[PaymentAccountAlreadyExists, PaymentAccount]] =
      findPaymentAccountByNumber(account.number).flatMap {
        case Some(value) => PaymentAccountAlreadyExists().asLeft.pure
        case None =>
          createPaymentAccountSql(account)
            .withUniqueGeneratedKeys[PaymentAccountId]("id")
            .map((id: PaymentAccountId) =>
              PaymentAccount(
                id,
                account.number,
                account.bic,
                account.swift,
                account.currency,
                account.creationDate,
                account.naturalPersonId,
                account.legalPersonId
              ).asRight
            )
      }

  }

  def make: AccountSql = new AccountSqlImpl

}
