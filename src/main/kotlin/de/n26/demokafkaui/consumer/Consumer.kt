package de.n26.demokafkaui.consumer

import de.n26.demokafkaui.BaseEvent
import de.n26.demokafkaui.Transactions
import de.n26.demokafkaui.UserTransaction
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import java.util.function.Consumer

@Component
class Consumer {
    private val logger = LoggerFactory.getLogger(Consumer::class.java)

    @KafkaListener(topics = ["wrapped_transactions"])
    fun listenWrappedTransactionsWithHeaders(
        @Payload message: BaseEvent,
        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) partition: Int
    ) {
        logger.info(
            """
                |Received Message with event:
                |${UserTransaction.parseFrom(message.event.value)}
                |from partition: $partition""".trimMargin()
        )
    }
}
