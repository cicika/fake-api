package fakeapi.http

import akka.actor.{Actor, ActorRef}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.util.{Success, Failure}

import spray.http.StatusCodes
import spray.routing.{Directives, HttpService}

trait FakeAPIRoute extends HttpService{
	
	implicit val timeout = Timeout(120 seconds)
	def fakeApi(responseActor: ActorRef) = {
		(post | get | put | delete) { 
			detach(){ ctx =>
				(responseActor ? GetResponse) onComplete {
					case Success(e) => ctx.complete(StatusCodes.OK)
					case Failure(ex) => ctx.complete(StatusCodes.InternalServerError)
				}
			}
		} 
	}
}

class FakeAPIService extends Actor with FakeAPIRoute{
	
	def actorRefFactory = context
	val responseActor = context.actorFor("/user/response-actor")	
	def receive = runRoute(fakeApi(responseActor))
}

