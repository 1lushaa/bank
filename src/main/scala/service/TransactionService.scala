package service

import cats.effect.IO
import cats.implicits.given
import dao.TransactionSql
import domain.error.TransactionError
import domain.error.TransactionError.TransactionsInternalError
import domain.transaction.CreateTransaction.CreateTransferBetweenAccounts
import domain.transaction.Transaction.TransferBetweenAccounts
import domain.transaction.{Transaction, TransactionId}
import doobie.Transactor
import doobie.implicits.toConnectionIOOps

trait TransactionService {

  def getTransferBetweenAccountsById(
    id: TransactionId
  ): IO[Either[TransactionsInternalError, Option[TransferBetweenAccounts]]]

  def createTransferBetweenAccounts(
    transfer: CreateTransferBetweenAccounts
  ): IO[Either[TransactionsInternalError, TransferBetweenAccounts]]

}

object TransactionService {

  private final class TransactionServiceImpl(
    transactionSql: TransactionSql,
    transactor: Transactor[IO]
  ) extends TransactionService {

    override def getTransferBetweenAccountsById(
      id: TransactionId
    ): IO[Either[TransactionsInternalError, Option[TransferBetweenAccounts]]] =
      transactionSql
        .findTransferBetweenAccountsById(id)
        .transact(transactor)
        .attempt
        .map(_.leftMap(TransactionsInternalError.apply))

    override def createTransferBetweenAccounts(
      transfer: CreateTransferBetweenAccounts
    ): IO[Either[TransactionsInternalError, TransferBetweenAccounts]] =
      transactionSql
        .createTransferBetweenAccounts(transfer)
        .transact(transactor)
        .attempt
        .map(_.leftMap(TransactionsInternalError.apply))

  }

  def make(transactor: Transactor[IO]): TransactionService =
    new TransactionServiceImpl(TransactionSql.make, transactor)

}
