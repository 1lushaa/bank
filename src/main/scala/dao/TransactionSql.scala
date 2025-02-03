package dao

import domain.transaction.*
import domain.transaction.CreateTransaction.CreateTransferBetweenAccounts
import domain.transaction.Transaction.*
import doobie.free.connection.ConnectionIO
import doobie.implicits.toSqlInterpolator
import doobie.{Query0, Update0}

trait TransactionSql {

  def findTransferBetweenAccountsById(
    id: TransactionId
  ): ConnectionIO[Option[TransferBetweenAccounts]]

  def createTransferBetweenAccounts(
    transfer: CreateTransferBetweenAccounts
  ): ConnectionIO[TransferBetweenAccounts]

}

object TransactionSql {

  object sqls {

    def findTransferBetweenAccountsByIdSql(id: TransactionId): Query0[TransferBetweenAccounts] =
      sql"select * from transfers_between_accounts where id=${id.value}"
        .query[TransferBetweenAccounts]

    def createTransfersBetweenAccountsSql(transfer: CreateTransferBetweenAccounts): Update0 =
      sql"""
           insert into transfers_between_accounts
           (sender_account_id, recipient_account_id, amount, currency, time)
           value
           (${transfer.senderAccountId.value}, ${transfer.recipientAccountId.value}, 
           ${transfer.amount}, ${transfer.currency.toString}, ${transfer.time.toEpochMilli})
         """.update

  }

  private final class TransactionSqlImpl extends TransactionSql {

    import sqls.*

    override def findTransferBetweenAccountsById(
      id: TransactionId
    ): ConnectionIO[Option[TransferBetweenAccounts]] = findTransferBetweenAccountsByIdSql(id).option

    override def createTransferBetweenAccounts(
      transfer: CreateTransferBetweenAccounts
    ): ConnectionIO[TransferBetweenAccounts] =
      createTransfersBetweenAccountsSql(transfer)
        .withUniqueGeneratedKeys[TransactionId]("id")
        .map((id: TransactionId) =>
          TransferBetweenAccounts(
            id,
            transfer.senderAccountId,
            transfer.recipientAccountId,
            transfer.amount,
            transfer.currency,
            transfer.time
          )
        )

  }

  def make: TransactionSql = new TransactionSqlImpl

}
