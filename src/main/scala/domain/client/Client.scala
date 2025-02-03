package domain.client

import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

import java.time.LocalDate

enum Client {

  case NaturalPerson(
    id: NaturalPersonId,
    name: ClientName,
    birthDate: LocalDate,
    passport: ClientPassport,
    sex: ClientSex,
    address: ClientAddress,
    phoneNumber: ClientPhoneNumber,
    email: ClientEmail
  )

  case LegalPerson(
    id: LegalPersonId,
    organizationName: ClientOrganizationName,
    organizationAddress: ClientAddress,
    inn: ClientINN,
    kpp: ClientKPP,
    ogrn: ClientOGRN,
    phoneNumber: ClientPhoneNumber,
    email: ClientEmail
  )

}

object Client {

  given Encoder[NaturalPerson] = deriveEncoder

  given Decoder[NaturalPerson] = deriveDecoder

  given Encoder[LegalPerson] = deriveEncoder

  given Decoder[LegalPerson] = deriveDecoder

}
