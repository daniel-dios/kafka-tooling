package de.n26.demokafkaui.config

import de.n26.demokafkaui.producer.Producer
import de.n26.demokafkaui.Transactions
import de.n26.demokafkaui.protoserializer.KafkaProtoDeserializer
import de.n26.demokafkaui.protoserializer.KafkaProtoSerializer
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.DefaultKafkaConsumerFactory
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.core.ConsumerFactory

@Configuration
class KafkaConfig {

    @Bean
    fun producer(
        @Qualifier("transactionsTopic") topic: NewTopic,
        template: KafkaTemplate<String, Transactions.userTransaction>
    ) = Producer(topic, template)

    @Bean(name = ["transactionsTopic"])
    fun transactionsTopic(
        @Value("\${topics.transactions.name}") name: String,
        @Value("\${topics.transactions.partitions}") partitions: String,
    ): NewTopic = NewTopic(name, partitions.toInt(), partitions.toShort())

    @Bean
    fun producerFactory(
        kafkaProperties: KafkaProperties
    ): ProducerFactory<String, Transactions.userTransaction> = DefaultKafkaProducerFactory(
        kafkaProperties.buildProducerProperties(),
        StringSerializer(),
        KafkaProtoSerializer()
    )

    @Bean
    fun kafkaTemplate(producerFactory: ProducerFactory<String, Transactions.userTransaction>) =
        KafkaTemplate(producerFactory)

    @Bean
    fun consumerFactory(
        kafkaProperties: KafkaProperties
    ): ConsumerFactory<String?, Transactions.userTransaction?> = DefaultKafkaConsumerFactory(
        kafkaProperties.buildConsumerProperties(),
        StringDeserializer(),
        KafkaProtoDeserializer(Transactions.userTransaction.parser())
    )

    @Bean
    fun kafkaListenerContainerFactory(
        consumerFactory: ConsumerFactory<String, Transactions.userTransaction>
    ): ConcurrentKafkaListenerContainerFactory<String, Transactions.userTransaction>? {
        val factory = ConcurrentKafkaListenerContainerFactory<String, Transactions.userTransaction>()
        factory.consumerFactory = consumerFactory
        return factory
    }
}
