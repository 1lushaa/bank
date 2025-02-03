package domain.client

import io.circe
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}

import java.time.LocalDate

enum CreateClient {

  case CreateNaturalPerson(
    name: ClientName,
    birthDate: LocalDate,
    passport: ClientPassport,
    sex: ClientSex,
    address: ClientAddress,
    phoneNumber: ClientPhoneNumber,
    email: ClientEmail
  )

  case CreateLegalPerson(
    organizationName: ClientOrganizationName,
    address: ClientAddress,
    inn: ClientINN,
    kpp: ClientKPP,
    ogrn: ClientOGRN,
    phoneNumber: ClientPhoneNumber,
    email: ClientEmail
  )

}

object CreateClient {

  given Encoder[CreateNaturalPerson] = deriveEncoder

  given Encoder[CreateLegalPerson] = deriveEncoder

  given Decoder[CreateNaturalPerson] = deriveDecoder

  given Decoder[CreateLegalPerson] = deriveDecoder

}
