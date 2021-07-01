package de.n26.demokafkaui.config

import de.n26.demokafkaui.Consumer
import de.n26.demokafkaui.Producer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfiguration {

    @Bean
    fun producer() = Producer()

    @Bean
    fun consumer() = Consumer()
}
