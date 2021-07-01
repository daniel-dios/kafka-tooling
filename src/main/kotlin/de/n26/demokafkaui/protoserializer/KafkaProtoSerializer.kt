package de.n26.demokafkaui.protoserializer

import com.google.protobuf.MessageLite
import org.apache.kafka.common.serialization.Serializer

class KafkaProtoSerializer<T : MessageLite?> : Serializer<T> {

    override fun configure(configs: Map<String?, *>?, isKey: Boolean) {}

    override fun serialize(topic: String, data: T): ByteArray {
        return data!!.toByteArray()
    }

    override fun close() {}
}