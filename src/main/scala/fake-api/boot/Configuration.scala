package fakeapi.boot

//import fakeapi.common._

import com.typesafe.config.ConfigFactory

trait FakeAPIConfiguration {

	val config = ConfigFactory.load()

	val FAKE_API_HOST = config.getString("fakeapi.http.host")
	val FAKE_API_PORT = config.getInt("fakeapi.http.port")
}