package fakeapi.model

import spray.http.{StatusCodes, HttpMethod}

sealed trait FakeAPIConfig

/*
- endpoint name
- method
- max latency
- spike duration
- latency distribution
- status codes
- status code distribution
- response bodies
*/

trait Distribution[T]
trait Probability

//case class StatusCodeDistribution(configMap: Map[StatusCodes, Probability]) extends Distribution[StatusCodes]{}

case class EndpointConfig(name: String, method: HttpMethod, maxLatency: Int, spike: Option[Int]) extends FakeAPIConfig {

}

object EndpointConfig {
	def pathRegex(input: String) = input.r
}