package de.n26.demokafkaui

import com.google.protobuf.Timestamp
import org.apache.kafka.clients.admin.NewTopic
import org.apache.kafka.common.internals.Topic
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders.MESSAGE_KEY
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import org.springframework.scheduling.annotation.Scheduled
import java.time.Instant
import java.util.*
import kotlin.random.Random.Default.nextInt

class Producer(
    private val topic: NewTopic,
    private val kafkaTemplate: KafkaTemplate<String, Transactions.userTransaction>
) {

    @Scheduled(fixedDelay = 1000, initialDelay = 5000)
    fun sendHello() {
        val userTransactionProto = Transactions.userTransaction
            .newBuilder()
            .setUserId(UUID.randomUUID().toString())
            .setAmount(nextInt(-100, 100))
            .setAt(Instant.now().toProtoTimestamp())
            .build()

        kafkaTemplate.send(topic.name(), userTransactionProto.userId, userTransactionProto)
    }

    private fun Instant.toProtoTimestamp() = Timestamp
        .newBuilder()
        .setSeconds(this.epochSecond)
        .setNanos(this.nano)
        .build()
}
