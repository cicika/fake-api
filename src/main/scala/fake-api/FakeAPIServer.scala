package fakeapi

import akka.actor._
import akka.io.IO

import boot._
import common._
import http._

import spray.can.Http

object FakeAPIServer extends App with FakeAPIKernel
								 with FakeAPIConfiguration {

	override def main(args: Array[String]) = {

		val apiService = system.actorOf(Props[FakeAPIService], "api-service")
		val responseActor = system.actorOf(Props[LatencyActor], "response-actor")

		IO(Http) ! Http.Bind(apiService, interface = FAKE_API_HOST, port = FAKE_API_PORT)
	}
}