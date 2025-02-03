package domain.transaction

import domain.account.{Currency, PaymentAccountId}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

import java.time.Instant

enum CreateTransaction {

  case CreateTransferBetweenAccounts(
    senderAccountId: PaymentAccountId,
    recipientAccountId: PaymentAccountId,
    amount: BigDecimal,
    currency: Currency,
    time: Instant
  )

}

object CreateTransaction {

  given Encoder[CreateTransferBetweenAccounts] = deriveEncoder

  given Decoder[CreateTransferBetweenAccounts] = deriveDecoder

}
