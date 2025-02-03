package domain

import io.circe.{Decoder, Encoder}
import sttp.tapir.{FieldName, Schema, SchemaType}

package object error {

  given Encoder[Throwable] = Encoder.encodeString.contramap(_.getMessage)

  given Decoder[Throwable] = Decoder.decodeString.map(new Throwable(_))

  given Schema[Throwable] = Schema(
    SchemaType.SProduct(
      List(
        SchemaType.SProductField[Throwable, String](
          FieldName("message", "message"),
          Schema.schemaForString,
          error => Some(error.getMessage)
        )
      )
    )
  )

}
