import doobie.Read

import java.time.{Instant, LocalDate}

package object dao {

  given Read[LocalDate] = Read[String].map(LocalDate.parse)
  
  given Read[Instant] = Read[Long].map(Instant.ofEpochMilli)

}
