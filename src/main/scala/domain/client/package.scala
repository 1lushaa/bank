package domain

import doobie.Read
import io.circe.derivation.{ConfiguredDecoder, ConfiguredEncoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.syntax.*
import io.circe.{Decoder, Encoder, Json}
import sttp.tapir.CodecFormat.TextPlain
import sttp.tapir.{Codec, Schema}

package object client {

  enum ClientSex {

    case Male
    case Female

  }

  object ClientSex {

    given Read[ClientSex] = Read[String].map(str =>
      str.toLowerCase() match
        case "male"   => ClientSex.Male
        case "female" => ClientSex.Female
        case other    => throw new IllegalArgumentException(s"Unknown client sex \'$other\'.")
    )

    given Encoder[ClientSex] = sex => Json.obj("sex" -> sex.asJson)

    given Decoder[ClientSex] = cursor => cursor.downField("sex").as[String].map(ClientSex.valueOf)

  }

  final case class ClientAddress(
    country: String,
    city: String,
    street: String,
    house: String,
    flat: Option[Int]
  )

  object ClientAddress {

    given Encoder[ClientAddress] = deriveEncoder

    given Decoder[ClientAddress] = deriveDecoder

  }

  opaque type NaturalPersonId = Long

  object NaturalPersonId {

    def apply(id: Long): NaturalPersonId = id

    extension (id: NaturalPersonId) def value: Long = id

    given Read[NaturalPersonId] = Read[Long].map(NaturalPersonId.apply)

    given Codec[String, NaturalPersonId, TextPlain] = Codec.long.map(NaturalPersonId.apply)(_.value)

    given Encoder[NaturalPersonId] = id => Json.obj("id" -> id.asJson)

    given Decoder[NaturalPersonId] = cursor => cursor.get[Long]("id").map(NaturalPersonId.apply)

    given Schema[NaturalPersonId] = Schema.schemaForLong

  }

  opaque type LegalPersonId = Long

  object LegalPersonId {

    def apply(id: Long): LegalPersonId = id

    extension (id: LegalPersonId) def value: Long = id

    given Read[LegalPersonId] = Read[Long].map(LegalPersonId.apply)

    given Codec[String, LegalPersonId, TextPlain] = Codec.long.map(LegalPersonId.apply)(_.value)

    given Encoder[LegalPersonId] = id => Json.obj("id" -> id.asJson)

    given Decoder[LegalPersonId] = cursor => cursor.get[Long]("id").map(LegalPersonId.apply)

    given Schema[LegalPersonId] = Schema.schemaForLong

  }

  final case class ClientName(surname: String, name: String, patronymic: Option[String])

  object ClientName {

    given Encoder[ClientName] = deriveEncoder

    given Decoder[ClientName] = deriveDecoder

  }

  final case class ClientPassport(series: String, number: String)

  object ClientPassport {

    given Encoder[ClientPassport] = deriveEncoder

    given Decoder[ClientPassport] = deriveDecoder

  }

  opaque type ClientPhoneNumber = String

  object ClientPhoneNumber {

    def apply(number: String): ClientPhoneNumber = number

    extension (phone: ClientPhoneNumber) def value: String = phone

    given Read[ClientPhoneNumber] = Read[String].map(ClientPhoneNumber.apply)

    given Encoder[ClientPhoneNumber] = phone => Json.obj("phone" -> phone.asJson)

    given Decoder[ClientPhoneNumber] = cursor =>
      cursor.get[String]("phone").map(ClientPhoneNumber.apply)

    given Schema[ClientPhoneNumber] = Schema.schemaForString

  }

  opaque type ClientEmail = String

  object ClientEmail {

    def apply(email: String): ClientEmail = email

    extension (email: ClientEmail) def value: String = email

    given Read[ClientEmail] = Read[String].map(ClientEmail.apply)

    given Encoder[ClientEmail] = email => Json.obj("email" -> email.asJson)

    given Decoder[ClientEmail] = cursor => cursor.get[String]("email").map(ClientEmail.apply)

    given Schema[ClientEmail] = Schema.schemaForString

  }

  opaque type ClientOrganizationName = String

  object ClientOrganizationName {

    def apply(name: String): ClientOrganizationName = name

    extension (name: ClientOrganizationName) def value: String = name

    given Read[ClientOrganizationName] = Read[String].map(ClientOrganizationName.apply)

    given Encoder[ClientOrganizationName] = organization =>
      Json.obj("organization" -> organization.asJson)

    given Decoder[ClientOrganizationName] = cursor =>
      cursor.get[String]("organization").map(ClientOrganizationName.apply)

    given Schema[ClientOrganizationName] = Schema.schemaForString

  }

  opaque type ClientINN = Long

  object ClientINN {

    def apply(inn: Long): ClientINN = inn

    extension (inn: ClientINN) def value: Long = inn

    given Read[ClientINN] = Read[Long].map(ClientINN.apply)

    given Codec[String, ClientINN, TextPlain] = Codec.long.map(ClientINN.apply)(_.value)

    given Encoder[ClientINN] = inn => Json.obj("inn" -> inn.asJson)

    given Decoder[ClientINN] = cursor => cursor.get[Long]("inn").map(ClientINN.apply)

    given Schema[ClientINN] = Schema.schemaForLong

  }

  opaque type ClientKPP = Int

  object ClientKPP {

    def apply(kpp: Int): ClientKPP = kpp

    extension (kpp: ClientKPP) def value: Long = kpp

    given Read[ClientKPP] = Read[Int].map(ClientKPP.apply)

    given Encoder[ClientKPP] = kpp => Json.obj("kpp" -> kpp.asJson)

    given Decoder[ClientKPP] = cursor => cursor.get[Int]("kpp").map(ClientKPP.apply)

    given Schema[ClientKPP] = Schema.schemaForInt

  }

  opaque type ClientOGRN = Long

  object ClientOGRN {

    def apply(ogrn: Long): ClientOGRN = ogrn

    extension (ogrn: ClientOGRN) def value: Long = ogrn

    given Read[ClientOGRN] = Read[Long].map(ClientOGRN.apply)

    given Encoder[ClientOGRN] = ogrn => Json.obj("ogrn" -> ogrn.asJson)

    given Decoder[ClientOGRN] = cursor => cursor.get[Long]("ogrn").map(ClientOGRN.apply)

    given Schema[ClientOGRN] = Schema.schemaForLong

  }

}
