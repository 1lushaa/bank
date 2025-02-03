package domain

import doobie.Read
import io.circe.syntax.given
import io.circe.{Decoder, Encoder, Json}
import sttp.tapir.CodecFormat.TextPlain
import sttp.tapir.{Codec, Schema}

package object account {

  enum Currency {

    case RUB
    case USD
    case EUR

  }

  object Currency {

    given Read[Currency] = Read[String].map(str =>
      str.toLowerCase match
        case "rub" => Currency.RUB
        case "usd" => Currency.USD
        case "eur" => Currency.EUR
        case other => throw new IllegalArgumentException(s"Unknown currency\'$other\'")
    )

    given Encoder[Currency] = currency => Json.obj("currency" -> currency.asJson)

    given Decoder[Currency] = cursor => cursor.get[String]("currency").map(Currency.valueOf)

  }

  opaque type PaymentAccountId = Long

  object PaymentAccountId {

    def apply(id: Long): PaymentAccountId = id

    extension (id: PaymentAccountId) def value: Long = id

    given Read[PaymentAccountId] = Read[Long].map(PaymentAccountId.apply)

    given Codec[String, PaymentAccountId, TextPlain] =
      Codec.long.map(PaymentAccountId.apply)(_.value)

    given Encoder[PaymentAccountId] = id => Json.obj("id" -> id.asJson)

    given Decoder[PaymentAccountId] = cursor => cursor.get[Long]("id").map(PaymentAccountId.apply)

    given Schema[PaymentAccountId] = Schema.schemaForLong

  }

  opaque type AccountNumber = String

  object AccountNumber {

    def apply(number: String): AccountNumber = number

    extension (number: AccountNumber) def value: String = number

    given Read[AccountNumber] = Read[String].map(AccountNumber.apply)

    given Codec[String, AccountNumber, TextPlain] =
      Codec.string.map(AccountNumber.apply)(_.value)

    given Encoder[AccountNumber] = number => Json.obj("number" -> number.asJson)

    given Decoder[AccountNumber] = cursor => cursor.get[String]("number").map(AccountNumber.apply)

    given Schema[AccountNumber] = Schema.schemaForString

  }

  opaque type AccountBIC = String

  object AccountBIC {

    def apply(bic: String): AccountBIC = bic

    extension (bic: AccountBIC) def value: String = bic

    given Read[AccountBIC] = Read[String].map(AccountBIC.apply)

    given Codec[String, AccountBIC, TextPlain] =
      Codec.string.map(AccountBIC.apply)(_.value)

    given Encoder[AccountBIC] = bic => Json.obj("bic" -> bic.asJson)

    given Decoder[AccountBIC] = cursor => cursor.get[String]("bic").map(AccountBIC.apply)

    given Schema[AccountBIC] = Schema.schemaForString

  }

  opaque type AccountSwift = String

  object AccountSwift {

    def apply(swift: String): AccountSwift = swift

    extension (swift: AccountSwift) def value: String = swift

    given Read[AccountSwift] = Read[String].map(AccountSwift.apply)

    given Codec[String, AccountSwift, TextPlain] =
      Codec.string.map(AccountSwift.apply)(_.value)

    given Encoder[AccountSwift] = swift => Json.obj("swift" -> swift.asJson)

    given Decoder[AccountSwift] = cursor => cursor.get[String]("swift").map(AccountSwift.apply)

    given Schema[AccountSwift] = Schema.schemaForString

  }

}
