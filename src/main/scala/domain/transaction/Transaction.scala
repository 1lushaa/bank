package domain.transaction

import domain.account.{Currency, PaymentAccountId}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

import java.time.Instant

enum Transaction {

  case TransferBetweenAccounts(
    transactionId: TransactionId,
    senderAccountId: PaymentAccountId,
    recipientAccountId: PaymentAccountId,
    amount: BigDecimal,
    currency: Currency,
    time: Instant
  )

}

object Transaction {

  given Encoder[TransferBetweenAccounts] = deriveEncoder

  given Decoder[TransferBetweenAccounts] = deriveDecoder

}
