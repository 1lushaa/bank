package domain.account

import domain.client.{LegalPersonId, NaturalPersonId}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

import java.time.LocalDate

enum CreateAccount {

  case CreatePaymentAccount(
    number: AccountNumber,
    bic: AccountBIC,
    swift: AccountSwift,
    currency: Currency,
    creationDate: LocalDate,
    naturalPersonId: NaturalPersonId,
    legalPersonId: LegalPersonId
  )

}

object CreateAccount {

  given Encoder[CreatePaymentAccount] = deriveEncoder

  given Decoder[CreatePaymentAccount] = deriveDecoder

}
