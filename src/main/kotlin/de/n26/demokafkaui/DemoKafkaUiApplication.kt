package de.n26.demokafkaui

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class DemoKafkaUiApplication

fun main(args: Array<String>) {
	runApplication<DemoKafkaUiApplication>(*args)
}
