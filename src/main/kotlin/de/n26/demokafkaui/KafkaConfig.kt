package de.n26.demokafkaui

import com.github.daniel.shuy.kafka.protobuf.serde.KafkaProtobufDeserializer
import com.github.daniel.shuy.kafka.protobuf.serde.KafkaProtobufSerializer
import de.n26.demokafkaui.Transactions
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.*

@Configuration
class KafkaConfig {

    @Bean
    fun topic1(): NewTopic = NewTopic("transactions", 1, 1.toShort())

    @Bean
    fun producerFactory(
        kafkaProperties: KafkaProperties
    ): ProducerFactory<String, Transactions.userTransaction> = DefaultKafkaProducerFactory(
        kafkaProperties.buildProducerProperties(),
        StringSerializer(),
        KafkaProtobufSerializer()
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
        KafkaProtobufDeserializer(Transactions.userTransaction.parser())
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
