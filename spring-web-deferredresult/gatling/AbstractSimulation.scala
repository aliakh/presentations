import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._

import scala.concurrent.duration._

abstract class AbstractSimulation extends Simulation {

  val rampUpTimeSecs = 60 seconds
  val testTimeSecs = 90 seconds
  val noOfUsers = 1000

  def baseName: String
  val requestName = baseName + "-request"
  val scenarioName = baseName + "-scenario"
  def URI: String

  val baseURL = "http://localhost:8080"

  val scn = scenario(scenarioName)
    .during(testTimeSecs) {
      exec(
        http(requestName)
          .get(URI)
          .check(status.is(200))
      )
    }

  val httpConf = http.baseURL(baseURL)
  setUp(scn.inject(rampUsers(noOfUsers) over rampUpTimeSecs).protocols(httpConf))

}