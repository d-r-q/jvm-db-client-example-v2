package ru.nsu.dbexample

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Import

@Import(Config::class)
@EnableAutoConfiguration(exclude=[FlywayAutoConfiguration::class])
@SpringBootApplication
class DbExampleApplication

fun main(args: Array<String>) {
	runApplication<DbExampleApplication>(*args)
}
