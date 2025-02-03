package domain.account

import domain.client.{LegalPersonId, NaturalPersonId}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

import java.time.LocalDate

enum Account {

  case PaymentAccount(
    id: PaymentAccountId,
    number: AccountNumber,
    bic: AccountBIC,
    swift: AccountSwift,
    currency: Currency,
    creationDate: LocalDate,
    naturalPersonId: NaturalPersonId,
    legalPersonId: LegalPersonId
  )

}

object Account {

  given Encoder[PaymentAccount] = deriveEncoder

  given Decoder[PaymentAccount] = deriveDecoder

}
