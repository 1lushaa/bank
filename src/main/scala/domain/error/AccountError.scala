package domain.error

import cats.syntax.option.given
import io.circe.syntax.given
import io.circe.{Decoder, Encoder, Json}

enum AccountError(val message: String, val cause: Option[Throwable] = None) {

  case PaymentAccountAlreadyExists() extends AccountError("Payment account already exists")

  case AccountsInternalError(cause0: Throwable)
      extends AccountError("Internal accounts error", cause0.some)

}

object AccountError {

  given Encoder[PaymentAccountAlreadyExists] = error => Json.obj("message" -> error.message.asJson)

  given Decoder[PaymentAccountAlreadyExists] = cursor =>
    cursor.get[String]("message").map(_ => PaymentAccountAlreadyExists())

  given Encoder[AccountsInternalError] = error => Json.obj("message" -> error.message.asJson)

  given Decoder[AccountsInternalError] = cursor =>
    cursor.get[String]("message").map(_ => AccountsInternalError(new Throwable()))

  given Encoder[AccountError] = {
    case error: PaymentAccountAlreadyExists => Encoder[PaymentAccountAlreadyExists].apply(error)
    case error: AccountsInternalError       => Encoder[AccountsInternalError].apply(error)
  }

  given Decoder[AccountError] = cursor =>
    cursor.get[String]("message").map {
      case "Payment account already exists" => PaymentAccountAlreadyExists()
      case "Internal accounts error"        => AccountsInternalError(new Throwable())
    }

}
