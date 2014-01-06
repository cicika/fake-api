package fakeapi.http

import akka.actor._
import akka.agent._
import akka.event.Logging

import fakeapi.common._

import com.typesafe.config.ConfigFactory

import scala.collection.JavaConverters._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.math._
import scala.util.Random

case class ActivateSpike(duration: Int)
case class GetResponse()
case class TimesUp()
case class Spike(active: Boolean, duration: Int = 0)

trait Latency {

	val perPathLatencyEnabled = config.getBoolean("fakeapi.latency.enabled")
	//val customStatusCodesEnabled = config.getBoolean("fakeapi.status-codes.enabled")
	//val customResponseBodiesEnabled = config.getBoolean("fakeapi.response-bodies.enabled")


	val defaultMaxLatency = config.getInt("fakeapi.defaults.latency.max-duration")
	//val defaultStatusCode = config.getInt("fakeapi.defaults.status-codes.code")
	//val defaultResponseBody = config.getString("fakeapi.defaults.response")
	//TODO::
	//val defaultDistribution = config.getList("fakeapi.defaults.latency.distribution").asScala	

	val defaultMaxSpike = config.getInt("fakeapi.defaults.latency.spike")
	val spikeEnabled = config.getBoolean("fakeapi.defaults.latency.spike-enabled")

	//TODO::
	//there will be a config object floating around to be used
	val maxLatency = defaultMaxLatency
	
	def latency = {abs(Random.nextGaussian) * maxLatency}.toInt 

}

class LatencyActor extends Actor with Latency{
	private val log = Logging(context.system, this)
	val spikeAgent = Agent(Spike(false))

	def receive = {
		case ActivateSpike(duration: Int) => spikeAgent send(Spike(true,duration))
		case GetResponse => 
			val sp = spikeAgent.apply()
			val delay = sp.active match {
				case true => 
					spikeAgent send(Spike(false))
					sp.duration
				case false => {
					val d = latency
					if(d > 0.95*maxLatency && d <= maxLatency) spikeAgent send(Spike(true, defaultMaxSpike))
					d
				}
			}
			log.info("Lagging this request for {} milliseconds", delay)
			context.system.scheduler.scheduleOnce(delay milliseconds, sender, TimesUp)(context.dispatcher, context.self)
		case _ =>
	}
}

