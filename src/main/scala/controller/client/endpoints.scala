package controller.client

import domain.client.*
import domain.client.Client.*
import domain.client.CreateClient.*
import domain.error.ClientError.ClientsInternalError
import domain.error.{*, given}
import sttp.tapir.*
import sttp.tapir.generic.auto.*
import sttp.tapir.json.circe.*

object endpoints {

  val findNaturalPersonById
    : PublicEndpoint[NaturalPersonId, ClientsInternalError, Option[NaturalPerson], Any] =
    endpoint.get
      .in("natural_persons" / path[NaturalPersonId])
      .errorOut(jsonBody[ClientsInternalError])
      .out(jsonBody[Option[NaturalPerson]])

  val findLegalPersonById
    : PublicEndpoint[LegalPersonId, ClientsInternalError, Option[LegalPerson], Any] =
    endpoint.get
      .in("legal_persons" / path[LegalPersonId])
      .errorOut(jsonBody[ClientsInternalError])
      .out(jsonBody[Option[LegalPerson]])

  val findNaturalPersonByPassport
    : PublicEndpoint[ClientPassport, ClientsInternalError, Option[NaturalPerson], Any] =
    endpoint.get
      .in("natural_persons" / jsonBody[ClientPassport])
      .errorOut(jsonBody[ClientsInternalError])
      .out(jsonBody[Option[NaturalPerson]])

  val findLegalPersonByINN
    : PublicEndpoint[ClientINN, ClientsInternalError, Option[LegalPerson], Any] =
    endpoint.get
      .in("legal_persons" / path[ClientINN])
      .errorOut(jsonBody[ClientsInternalError])
      .out(jsonBody[Option[LegalPerson]])

  val createNaturalPerson: PublicEndpoint[CreateNaturalPerson, ClientError, NaturalPerson, Any] =
    endpoint.post
      .in("natural_persons" / jsonBody[CreateNaturalPerson])
      .errorOut(jsonBody[ClientError])
      .out(jsonBody[NaturalPerson])

  val createLegalPerson: PublicEndpoint[CreateLegalPerson, ClientError, LegalPerson, Any] =
    endpoint.post
      .in("legal_persons" / jsonBody[CreateLegalPerson])
      .errorOut(jsonBody[ClientError])
      .out(jsonBody[LegalPerson])

}
