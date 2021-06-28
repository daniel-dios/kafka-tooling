package de.n26.demokafkaui

import de.n26.demokafkaui.Hello.wussup
import org.springframework.scheduling.annotation.Scheduled
import reactor.core.publisher.Flux
import reactor.core.publisher.Sinks
import reactor.core.publisher.Sinks.EmitFailureHandler.FAIL_FAST
import reactor.core.publisher.Sinks.Many
import java.time.Instant
import java.util.function.Supplier

class Producer : Supplier<Flux<ByteArray>> {

    private val sink: Many<ByteArray> = Sinks.many().unicast().onBackpressureBuffer()

    @Scheduled(fixedDelay = 1000, initialDelay = 5000)
    fun sendHello() {
        val build: wussup = wussup.newBuilder().setFoo("bar" + Instant.now()).build()

        synchronized(sink) {
            sink.emitNext(build.toByteArray(), FAIL_FAST)
        }
    }

    override fun get(): Flux<ByteArray> {
        return sink.asFlux()
    }
}
