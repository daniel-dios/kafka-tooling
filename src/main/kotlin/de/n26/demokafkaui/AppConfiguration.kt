package de.n26.demokafkaui

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfiguration {

    @Bean
    fun producer() = Producer()

    @Bean
    fun consumer() = Consumer()
}
