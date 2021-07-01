package de.n26.demokafkaui.producer

import com.google.protobuf.Timestamp
import de.n26.demokafkaui.Transactions
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.kafka.core.KafkaTemplate
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
        val userTransactionProto = Transactions.userTransaction.newBuilder()
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
