package de.n26.demokafkaui.producer

import com.google.protobuf.Any.pack
import com.google.protobuf.Timestamp
import de.n26.demokafkaui.BaseEvent
import de.n26.demokafkaui.UserTransaction
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.scheduling.annotation.Scheduled
import java.time.Instant
import java.util.UUID
import kotlin.random.Random.Default.nextInt

class Producer(
    private val wrappedTransactionsTemplate: KafkaTemplate<String, BaseEvent>,
    private val transactionsTemplate: KafkaTemplate<String, UserTransaction>,
) {

    @Scheduled(fixedDelay = 1000, initialDelay = 5000)
    fun sendWrappedTransaction() {
        val userTransaction = UserTransaction
            .newBuilder()
            .setUserId(UUID.randomUUID().toString())
            .setAt(Instant.now().toProtoTimestamp())
            .setAmount(nextInt(-100, 100))
            .build()

        val event = BaseEvent
            .newBuilder()
            .setEvent(pack(userTransaction))
            .setEventId(UUID.randomUUID().toString())
            .setEventTimestamp(Instant.now().toEpochMilli())
            .build()

        wrappedTransactionsTemplate.send("wrapped_transactions", event.eventId, event)
    }

    @Scheduled(fixedDelay = 1000, initialDelay = 5000)
    fun sendTransaction() {
        val userTransaction = UserTransaction
            .newBuilder()
            .setUserId(UUID.randomUUID().toString())
            .setAt(Instant.now().toProtoTimestamp())
            .setAmount(nextInt(-100, 100))
            .build()

        transactionsTemplate.send("transactions", userTransaction.userId, userTransaction)
    }

    private fun Instant.toProtoTimestamp() = Timestamp
        .newBuilder()
        .setSeconds(this.epochSecond)
        .setNanos(this.nano)
        .build()
}
