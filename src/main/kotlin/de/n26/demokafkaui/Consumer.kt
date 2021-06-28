package de.n26.demokafkaui

import org.slf4j.LoggerFactory
import java.util.function.Consumer

class Consumer : Consumer<ByteArray> {
    private val logger = LoggerFactory.getLogger(Consumer::class.java)

    override fun accept(bytes: ByteArray) {
        logger.info("Received: {}", Transactions.userTransaction.parseFrom(bytes))
    }
}
