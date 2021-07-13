package de.n26.demokafkaui.producer

import com.google.protobuf.Any.pack
import com.google.protobuf.Timestamp
import de.n26.demokafkaui.Transactions
import de.n26.demokafkaui.event.CustomBaseEvent
import org.apache.kafka.clients.admin.NewTopic
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.scheduling.annotation.Scheduled
import java.time.Instant
import java.util.UUID
import kotlin.random.Random.Default.nextInt

class Producer(
    private val topic: NewTopic,
    private val kafkaTemplate: KafkaTemplate<String, CustomBaseEvent>
) {

    @Scheduled(fixedDelay = 1000, initialDelay = 5000)
    fun sendHello() {
        val userTransaction = Transactions.userTransaction
            .newBuilder()
            .setUserId(UUID.randomUUID().toString())
            .setAt(Instant.now().toProtoTimestamp())
            .setAmount(nextInt(-100, 100))
            .build()

        val event = CustomBaseEvent
            .newBuilder()
            .setEvent(pack(userTransaction))
            .setEventId(UUID.randomUUID().toString())
            .setEventTimestamp(Instant.now().toEpochMilli())
            .build()

        kafkaTemplate.send(topic.name(), event.eventId, event)
    }

    private fun Instant.toProtoTimestamp() = Timestamp
        .newBuilder()
        .setSeconds(this.epochSecond)
        .setNanos(this.nano)
        .build()
}
