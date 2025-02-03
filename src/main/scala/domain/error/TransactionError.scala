package domain.error

import cats.syntax.option.given
import io.circe.syntax.given
import io.circe.{Decoder, Encoder, Json}

enum TransactionError(val message: String, val cause: Option[Throwable] = None) {

  case TransactionsInternalError(cause0: Throwable)
      extends TransactionError("Internal transactions error", cause0.some)

}

object TransactionError {

  given Encoder[TransactionsInternalError] = error => Json.obj("message" -> error.message.asJson)

  given Decoder[TransactionsInternalError] = cursor =>
    cursor.get[String]("message").map(_ => TransactionsInternalError(new Throwable()))

  given Encoder[TransactionError] = { case error: TransactionsInternalError =>
    Encoder[TransactionsInternalError].apply(error)
  }

  given Decoder[TransactionError] = cursor =>
    cursor.get[String]("message").map { case "Internal transactions error" =>
      TransactionsInternalError(new Throwable())
    }

}
