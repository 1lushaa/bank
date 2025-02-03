package controller.client

import cats.effect.IO
import service.ClientService
import sttp.tapir.server.ServerEndpoint

trait ClientController {

  def findNaturalPersonById: ServerEndpoint[Any, IO]

  def findLegalPersonById: ServerEndpoint[Any, IO]

  def findNaturalPersonByPassport: ServerEndpoint[Any, IO]

  def findLegalPersonByINN: ServerEndpoint[Any, IO]

  def createNaturalPerson: ServerEndpoint[Any, IO]

  def createLegalPerson: ServerEndpoint[Any, IO]

  def getAllEndpoints: List[ServerEndpoint[Any, IO]]

}

object ClientController {

  private final class ClientControllerImpl(service: ClientService) extends ClientController {

    override def findNaturalPersonById: ServerEndpoint[Any, IO] =
      endpoints.findNaturalPersonById.serverLogic(id => service.findNaturalPersonById(id))

    override def findLegalPersonById: ServerEndpoint[Any, IO] =
      endpoints.findLegalPersonById.serverLogic(id => service.findLegalPersonById(id))

    override def findNaturalPersonByPassport: ServerEndpoint[Any, IO] =
      endpoints.findNaturalPersonByPassport.serverLogic(passport =>
        service.findNaturalPersonByPassport(passport)
      )

    override def findLegalPersonByINN: ServerEndpoint[Any, IO] =
      endpoints.findLegalPersonByINN.serverLogic(inn => service.findLegalPersonByINN(inn))

    override def createNaturalPerson: ServerEndpoint[Any, IO] =
      endpoints.createNaturalPerson.serverLogic(person => service.createNaturalPerson(person))

    override def createLegalPerson: ServerEndpoint[Any, IO] =
      endpoints.createLegalPerson.serverLogic(person => service.createLegalPerson(person))

    override def getAllEndpoints: List[ServerEndpoint[Any, IO]] =
      List(
        findNaturalPersonById,
        findLegalPersonById,
        findNaturalPersonByPassport,
        findLegalPersonByINN,
        createNaturalPerson,
        createLegalPerson
      )

  }

  def make(service: ClientService): ClientController = new ClientControllerImpl(service)

}
