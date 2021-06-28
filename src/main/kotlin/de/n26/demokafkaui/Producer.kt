package de.n26.demokafkaui

import com.google.protobuf.Timestamp
import org.springframework.scheduling.annotation.Scheduled
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import reactor.core.publisher.Sinks.EmitFailureHandler.FAIL_FAST
import reactor.core.publisher.Sinks.Many
import java.time.Instant
import java.util.*
import java.util.function.Supplier
import kotlin.random.Random.Default.nextInt

class Producer : Supplier<Flux<ByteArray>> {

    private val sink: Many<ByteArray> = Sinks.many().unicast().onBackpressureBuffer()

    @Scheduled(fixedDelay = 1000, initialDelay = 5000)
    fun sendHello() {
        val build = Hello.userTransaction
            .newBuilder()
            .setUserId(UUID.randomUUID().toString())
            .setAmount(nextInt(-100, 100))
            .setAt(Instant.now().toProtoTimestamp())
            .build()

        synchronized(sink) {
            sink.emitNext(build.toByteArray(), FAIL_FAST)
        }
    }

    override fun get(): Flux<ByteArray> = sink.asFlux()

    private fun Instant.toProtoTimestamp() = Timestamp
        .newBuilder()
        .setSeconds(this.epochSecond)
        .setNanos(this.nano)
        .build()
}
