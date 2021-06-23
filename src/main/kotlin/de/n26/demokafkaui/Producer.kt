package de.n26.demokafkaui

import org.springframework.scheduling.annotation.Scheduled
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import reactor.core.publisher.Sinks.EmitFailureHandler.FAIL_FAST
import reactor.core.publisher.Sinks.Many
import java.time.ZonedDateTime
import java.time.ZonedDateTime.now
import java.util.function.Supplier

data class MessageOut(val message: String, val time: String)

class Producer : Supplier<Flux<MessageOut>> {

    private val sink: Many<MessageOut> = Sinks.many().unicast().onBackpressureBuffer()

    @Scheduled(fixedDelay = 1000, initialDelay = 5000)
    fun sendHello() {
        synchronized(sink) {
            sink.emitNext(MessageOut(message = "hello!", time = now().toString()), FAIL_FAST)
        }
    }

    override fun get(): Flux<MessageOut> {
        return sink.asFlux()
    }
}
