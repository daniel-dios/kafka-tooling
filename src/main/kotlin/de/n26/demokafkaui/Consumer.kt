package de.n26.demokafkaui

import org.slf4j.LoggerFactory
import java.util.function.Consumer

data class MessageIn(val message: String, val time: String)

class Consumer : Consumer<MessageIn> {
    private val logger = LoggerFactory.getLogger(Consumer::class.java)

    override fun accept(messageIn: MessageIn) {
        logger.info("Received: {}", messageIn)
    }
}
