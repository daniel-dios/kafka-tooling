package de.n26.demokafkaui

import com.google.protobuf.Timestamp
import org.springframework.kafka.support.KafkaHeaders.MESSAGE_KEY
import org.springframework.messaging.Message
import org.springframework.messaging.support.MessageBuilder
import org.springframework.scheduling.annotation.Scheduled
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import reactor.core.publisher.Sinks.EmitFailureHandler.FAIL_FAST
import reactor.core.publisher.Sinks.Many
import java.time.Instant
import java.util.UUID
import java.util.function.Supplier
import kotlin.random.Random.Default.nextInt

class Producer : Supplier<Flux<Message<ByteArray>>> {

    private val sink: Many<Message<ByteArray>> = Sinks.many().unicast().onBackpressureBuffer()

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

        synchronized(sink) {
            sink.emitNext(springMessage, FAIL_FAST)
        }
    }

    override fun get(): Flux<Message<ByteArray>> = sink.asFlux()

    private fun Instant.toProtoTimestamp() = Timestamp
        .newBuilder()
        .setSeconds(this.epochSecond)
        .setNanos(this.nano)
        .build()
}
