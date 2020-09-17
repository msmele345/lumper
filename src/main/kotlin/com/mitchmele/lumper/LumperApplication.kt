package com.mitchmele.lumper

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LumperApplication

fun main(args: Array<String>) {
	runApplication<LumperApplication>(*args)
}
