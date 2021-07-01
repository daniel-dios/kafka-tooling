package de.n26.demokafkaui

import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import java.util.function.Consumer

class Consumer {
    private val logger = LoggerFactory.getLogger(Consumer::class.java)

    @KafkaListener(topics = ["transactions"])
    fun listenWithHeaders(
        @Payload message: Transactions.userTransaction,
        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) partition: Int
    ) {
        logger.info(
            "Received Message: " + message + "from partition: " + partition
        )
    }
}
