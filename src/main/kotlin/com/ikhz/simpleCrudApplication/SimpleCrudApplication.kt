package com.ikhz.simpleCrudApplication

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SimpleCrudApplication

fun main(args: Array<String>) {
	runApplication<SimpleCrudApplication>(*args)
}
