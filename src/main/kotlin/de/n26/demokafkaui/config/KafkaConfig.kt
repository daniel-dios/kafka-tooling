package de.n26.demokafkaui.config

import de.n26.demokafkaui.BaseEvent
import de.n26.demokafkaui.UserTransaction
import de.n26.demokafkaui.producer.Producer
import de.n26.demokafkaui.protoserializer.KafkaProtoDeserializer
import de.n26.demokafkaui.protoserializer.KafkaProtoSerializer
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory

@Configuration
class KafkaConfig {

    @Bean
    fun producer(
        wrappedTransactionsTemplate: KafkaTemplate<String, BaseEvent>,
    ) = Producer(wrappedTransactionsTemplate)

    @Bean
    fun wrappedTransactionProducerFactory(
        kafkaProperties: KafkaProperties
    ): ProducerFactory<String, BaseEvent> = DefaultKafkaProducerFactory(
        kafkaProperties.buildProducerProperties(),
        StringSerializer(),
        KafkaProtoSerializer()
    )

    @Bean
    fun wrappedTransactionTemplate(producerFactory: ProducerFactory<String, BaseEvent>) =
        KafkaTemplate(producerFactory)

    @Bean
    fun consumerFactory(
        kafkaProperties: KafkaProperties
    ): ConsumerFactory<String?, BaseEvent?> = DefaultKafkaConsumerFactory(
        kafkaProperties.buildConsumerProperties(),
        StringDeserializer(),
        KafkaProtoDeserializer(BaseEvent.parser())
    )

    @Bean
    fun kafkaListenerContainerFactory(
        consumerFactory: ConsumerFactory<String, BaseEvent>
    ): ConcurrentKafkaListenerContainerFactory<String, BaseEvent>? {
        val factory = ConcurrentKafkaListenerContainerFactory<String, BaseEvent>()
        factory.consumerFactory = consumerFactory
        return factory
    }
}
