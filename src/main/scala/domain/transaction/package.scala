package domain

import domain.account.PaymentAccountId
import doobie.Read
import io.circe.syntax.given
import io.circe.{Decoder, Encoder, Json}
import sttp.tapir.CodecFormat.TextPlain
import sttp.tapir.{Codec, Schema}

package object transaction {

  opaque type TransactionId = String

  object TransactionId {

    def apply(id: String): TransactionId = id

    extension (id: TransactionId) def value: String = id

    given Read[TransactionId] = Read[String].map(TransactionId.apply)

    given Codec[String, TransactionId, TextPlain] = Codec.string.map(TransactionId.apply)(_.value)

    given Encoder[TransactionId] = id => Json.obj("id" -> id.asJson)

    given Decoder[TransactionId] = cursor => cursor.get[String]("id").map(TransactionId.apply)

    given Schema[TransactionId] = Schema.schemaForString

  }

}
