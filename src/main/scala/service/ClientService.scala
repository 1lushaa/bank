package service

import cats.effect.IO
import cats.implicits.catsSyntaxEither
import cats.syntax.either.catsSyntaxEitherId
import dao.ClientSql
import domain.client
import domain.client.*
import domain.client.Client.*
import domain.client.CreateClient.{CreateLegalPerson, CreateNaturalPerson}
import domain.error.ClientError
import domain.error.ClientError.ClientsInternalError
import doobie.*
import doobie.implicits.given

trait ClientService {

  def findNaturalPersonById(
    id: NaturalPersonId
  ): IO[Either[ClientsInternalError, Option[NaturalPerson]]]

  def findLegalPersonById(id: LegalPersonId): IO[Either[ClientsInternalError, Option[LegalPerson]]]

  def findNaturalPersonByPassport(
    passport: ClientPassport
  ): IO[Either[ClientsInternalError, Option[NaturalPerson]]]

  def findLegalPersonByINN(
    inn: ClientINN
  ): IO[Either[ClientsInternalError, Option[LegalPerson]]]

  def createNaturalPerson(
    client: CreateNaturalPerson
  ): IO[Either[ClientError, NaturalPerson]]

  def createLegalPerson(
    client: CreateLegalPerson
  ): IO[Either[ClientError, LegalPerson]]

}

object ClientService {

  private final class ClientServiceImpl(clientSql: ClientSql, transactor: Transactor[IO])
      extends ClientService {

    override def findNaturalPersonById(
      id: NaturalPersonId
    ): IO[Either[ClientsInternalError, Option[NaturalPerson]]] =
      clientSql
        .findNaturalPersonById(id)
        .transact(transactor)
        .attempt
        .map(_.leftMap(ClientsInternalError.apply))

    override def findLegalPersonById(
      id: LegalPersonId
    ): IO[Either[ClientsInternalError, Option[LegalPerson]]] =
      clientSql
        .findLegalPersonById(id)
        .transact(transactor)
        .attempt
        .map(_.leftMap(ClientsInternalError.apply))

    override def findNaturalPersonByPassport(
      passport: ClientPassport
    ): IO[Either[ClientsInternalError, Option[NaturalPerson]]] =
      clientSql
        .findNaturalPersonByPassport(passport)
        .transact(transactor)
        .attempt
        .map(_.leftMap(ClientsInternalError.apply))

    override def findLegalPersonByINN(
      inn: ClientINN
    ): IO[Either[ClientsInternalError, Option[LegalPerson]]] =
      clientSql
        .findLegalPersonByINN(inn)
        .transact(transactor)
        .attempt
        .map(_.leftMap(ClientsInternalError.apply))

    override def createNaturalPerson(
      client: CreateNaturalPerson
    ): IO[Either[ClientError, NaturalPerson]] =
      clientSql.createNaturalPerson(client).transact(transactor).attempt.map {
        case Left(error)          => ClientsInternalError(error).asLeft
        case Right(Left(error))   => error.asLeft
        case Right(Right(person)) => person.asRight
      }

    override def createLegalPerson(
      client: CreateLegalPerson
    ): IO[Either[ClientError, LegalPerson]] =
      clientSql.createLegalPerson(client).transact(transactor).attempt.map {
        case Left(error)          => ClientsInternalError(error).asLeft
        case Right(Left(error))   => error.asLeft
        case Right(Right(person)) => person.asRight
      }

  }

  def make(transactor: Transactor[IO]): ClientService =
    new ClientServiceImpl(ClientSql.make, transactor)

}
