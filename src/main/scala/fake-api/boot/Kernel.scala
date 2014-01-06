package fakeapi.boot

import akka.actor.ActorSystem
import akka.kernel.Bootable

import fakeapi.common._

import com.typesafe.config.ConfigFactory

trait FakeAPIKernel extends Bootable{

	val systemName = config.getString("fakeapi.system")

	def systemDef: ActorSystem = ActorSystem("fakeapi", config)

	lazy implicit val system = systemDef

	def startup = {}

	def shutdown = {}
}