package dao

import cats.syntax.applicative.given
import cats.syntax.either.given
import domain.client.*
import domain.client.Client.*
import domain.client.CreateClient.*
import domain.error.ClientError.*
import doobie.*
import doobie.implicits.toSqlInterpolator

import java.time.LocalDate

trait ClientSql {

  def findNaturalPersonById(id: NaturalPersonId): ConnectionIO[Option[NaturalPerson]]

  def findLegalPersonById(id: LegalPersonId): ConnectionIO[Option[LegalPerson]]

  def findNaturalPersonByPassport(passport: ClientPassport): ConnectionIO[Option[NaturalPerson]]

  def findLegalPersonByINN(inn: ClientINN): ConnectionIO[Option[LegalPerson]]

  def createNaturalPerson(
    client: CreateNaturalPerson
  ): ConnectionIO[Either[NaturalPersonAlreadyExists, NaturalPerson]]

  def createLegalPerson(
    client: CreateLegalPerson
  ): ConnectionIO[Either[LegalPersonAlreadyExists, LegalPerson]]

}

object ClientSql {

  object sqls {

    def findNaturalPersonByIdSql(id: NaturalPersonId): Query0[NaturalPerson] =
      sql"select * from natural_person_clients where id=${id.value}".query[NaturalPerson]

    def findLegalPersonByIdSql(id: LegalPersonId): Query0[LegalPerson] =
      sql"select * from legal_person_clients where id=${id.value}".query[LegalPerson]

    def findNaturalPersonByPassportSql(passport: ClientPassport): Query0[NaturalPerson] =
      sql"""
            select *
            from natural_person_clients
            where passport_series=${passport.series} and passport_number=${passport.number}
           """.query[NaturalPerson]

    def findLegalPersonByINNSql(inn: ClientINN): Query0[LegalPerson] =
      sql"""
           select *
           from legal_person_clients
           where inn=${inn.value}
           """.query[LegalPerson]

    def createNaturalPersonSql(client: CreateNaturalPerson): Update0 =
      sql"""
            insert into natural_person_clients
            (surname, name, patronymic, birth_date, passport_series, passport_number,
            sex, country, city, street, house, flat, phone_number, email)
            value
            (${client.name.surname}, ${client.name.name}, ${client.name.patronymic}, ${client.birthDate.toString},
            ${client.passport.series}, ${client.passport.number}, ${client.sex.toString}, ${client.address.country},
            ${client.address.city}, ${client.address.street}, ${client.address.house}, ${client.address.flat},
            ${client.phoneNumber.value}, ${client.email.value})
            """.update

    def createLegalPersonSql(client: CreateLegalPerson): Update0 =
      sql"""
           insert into legal_person_clients
           (organization_name, country, city, street, house, flat, inn, kpp, ogrn, phone_number, email)
           value
           (${client.organizationName.value}, ${client.address.country}, ${client.address.city},
           ${client.address.street}, ${client.address.house}, ${client.address.flat}, ${client.inn.value},
           ${client.kpp.value}, ${client.ogrn.value}, ${client.phoneNumber.value}, ${client.email.value})
         """.update

  }

  private final class ClientSqlImpl extends ClientSql {

    import sqls.*

    override def findNaturalPersonById(id: NaturalPersonId): ConnectionIO[Option[NaturalPerson]] =
      findNaturalPersonByIdSql(id).option

    override def findLegalPersonById(id: LegalPersonId): ConnectionIO[Option[LegalPerson]] =
      findLegalPersonByIdSql(id).option

    override def findNaturalPersonByPassport(
      passport: ClientPassport
    ): ConnectionIO[Option[NaturalPerson]] =
      findNaturalPersonByPassportSql(passport).option

    override def findLegalPersonByINN(inn: ClientINN): ConnectionIO[Option[LegalPerson]] =
      findLegalPersonByINNSql(inn).option

    override def createNaturalPerson(
      client: CreateNaturalPerson
    ): ConnectionIO[Either[NaturalPersonAlreadyExists, NaturalPerson]] =
      findNaturalPersonByPassport(client.passport).flatMap {
        case Some(_) => NaturalPersonAlreadyExists().asLeft.pure
        case None =>
          createNaturalPersonSql(client)
            .withUniqueGeneratedKeys[NaturalPersonId]("id")
            .map((id: NaturalPersonId) =>
              NaturalPerson(
                id,
                client.name,
                client.birthDate,
                client.passport,
                client.sex,
                client.address,
                client.phoneNumber,
                client.email
              ).asRight
            )
      }

    override def createLegalPerson(
      client: CreateLegalPerson
    ): ConnectionIO[Either[LegalPersonAlreadyExists, LegalPerson]] =
      findLegalPersonByINN(client.inn).flatMap {
        case Some(_) => LegalPersonAlreadyExists().asLeft.pure
        case None =>
          createLegalPersonSql(client)
            .withUniqueGeneratedKeys[LegalPersonId]("id")
            .map((id: LegalPersonId) =>
              LegalPerson(
                id,
                client.organizationName,
                client.address,
                client.inn,
                client.kpp,
                client.ogrn,
                client.phoneNumber,
                client.email
              ).asRight
            )
      }

  }

  def make: ClientSql = new ClientSqlImpl

}
