package de.n26.demokafkaui.config

import com.github.daniel.shuy.kafka.protobuf.serde.KafkaProtobufDeserializer
import com.github.daniel.shuy.kafka.protobuf.serde.KafkaProtobufSerializer
import de.n26.demokafkaui.Transactions
import org.apache.kafka.clients.admin.AdminClientConfig
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.*

@Configuration
class KafkaConfig {

    private val bootstrapAddress = "localhost:9092"

    @Bean
    fun kafkaAdmin(): KafkaAdmin {
        val configs: MutableMap<String, Any?> = HashMap()
        configs[AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
        return KafkaAdmin(configs)
    }

    @Bean
    fun topic1(): NewTopic {
        return NewTopic("transactions", 1, 1.toShort())
    }

    @Bean
    fun producerFactory(): ProducerFactory<String, Transactions.userTransaction> {
        val configProps: MutableMap<String, Any> = HashMap()
        configProps[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress

        return DefaultKafkaProducerFactory(
            configProps,
            StringSerializer(),
            KafkaProtobufSerializer()
        )
    }

    @Bean
    fun kafkaTemplate(producerFactory: ProducerFactory<String, Transactions.userTransaction>) =
        KafkaTemplate(producerFactory)

    @Bean
    fun consumerFactory(): ConsumerFactory<String?, Transactions.userTransaction?> {
        val props: MutableMap<String, Any> = HashMap()
        props[ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapAddress
        props[ConsumerConfig.GROUP_ID_CONFIG] = "foo"
        return DefaultKafkaConsumerFactory(
            props,
            StringDeserializer(),
            KafkaProtobufDeserializer(Transactions.userTransaction.parser())
        )
    }

    @Bean
    fun kafkaListenerContainerFactory(): ConcurrentKafkaListenerContainerFactory<String, Transactions.userTransaction>? {
        val factory = ConcurrentKafkaListenerContainerFactory<String, Transactions.userTransaction>()
        factory.consumerFactory = consumerFactory()
        return factory
    }
}
