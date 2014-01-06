package fakeapi

import com.typesafe.config.ConfigFactory

package object common {

	val config = ConfigFactory.load()
}