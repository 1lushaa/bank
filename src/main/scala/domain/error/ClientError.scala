package domain.error

import cats.syntax.option.given
import io.circe.syntax.given
import io.circe.{Decoder, Encoder, Json}

enum ClientError(val message: String, val cause: Option[Throwable] = None) {

  case LegalPersonAlreadyExists()
      extends ClientError("Legal person with same information already exists")

  case NaturalPersonAlreadyExists()
      extends ClientError("Natural person with same information already exists")

  case ClientsInternalError(cause0: Throwable)
      extends ClientError("Internal clients error", cause0.some)

}

object ClientError {

  given Encoder[LegalPersonAlreadyExists] = error => Json.obj("message" -> error.message.asJson)

  given Decoder[LegalPersonAlreadyExists] = cursor =>
    cursor.get[String]("message").map(_ => LegalPersonAlreadyExists())

  given Encoder[NaturalPersonAlreadyExists] = error => Json.obj("message" -> error.message.asJson)

  given Decoder[NaturalPersonAlreadyExists] = cursor =>
    cursor.get[String]("message").map(_ => NaturalPersonAlreadyExists())

  given Encoder[ClientsInternalError] = error => Json.obj("message" -> error.message.asJson)

  given Decoder[ClientsInternalError] = cursor =>
    cursor.get[String]("message").map(_ => ClientsInternalError(new Throwable()))

  given Encoder[ClientError] = {
    case error: LegalPersonAlreadyExists   => Encoder[LegalPersonAlreadyExists].apply(error)
    case error: NaturalPersonAlreadyExists => Encoder[NaturalPersonAlreadyExists].apply(error)
    case error: ClientsInternalError       => Encoder[ClientsInternalError].apply(error)
  }

  given Decoder[ClientError] = cursor =>
    cursor.get[String]("message").map {
      case "Legal person with same information already exists"   => LegalPersonAlreadyExists()
      case "Natural person with same information already exists" => NaturalPersonAlreadyExists()
      case "Internal clients error" => ClientsInternalError(new Throwable())
    }

}
