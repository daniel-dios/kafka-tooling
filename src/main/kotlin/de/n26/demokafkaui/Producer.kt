package de.n26.demokafkaui

import com.google.protobuf.Timestamp
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.KafkaHeaders.MESSAGE_KEY
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.UUID
import kotlin.random.Random.Default.nextInt

@Component
class Producer {

    @Autowired
    private lateinit var kafkaTemplate: KafkaTemplate<String, Transactions.userTransaction>

    @Scheduled(fixedDelay = 1000, initialDelay = 5000)
    fun sendHello() {
        val userTransactionProto = Transactions.userTransaction
            .newBuilder()
            .setUserId(UUID.randomUUID().toString())
            .setAmount(nextInt(-100, 100))
            .setAt(Instant.now().toProtoTimestamp())
            .build()

        val springMessage: Message<ByteArray> = MessageBuilder
            .withPayload(userTransactionProto.toByteArray())
            .setHeader(MESSAGE_KEY, userTransactionProto.userId.toByteArray())
            .build()

        kafkaTemplate.send("transactions", userTransactionProto)
    }

    private fun Instant.toProtoTimestamp() = Timestamp
        .newBuilder()
        .setSeconds(this.epochSecond)
        .setNanos(this.nano)
        .build()
}
