package de.n26.demokafkaui.protoserializer

import com.google.protobuf.InvalidProtocolBufferException
import com.google.protobuf.MessageLite
import com.google.protobuf.Parser
import org.apache.kafka.common.errors.SerializationException
import org.apache.kafka.common.serialization.Deserializer

class KafkaProtoDeserializer<T : MessageLite?>(private val parser: Parser<T>) : Deserializer<T> {

    override fun configure(configs: Map<String?, *>?, isKey: Boolean) {}

    override fun deserialize(topic: String, data: ByteArray): T = try {
        parser.parseFrom(data)
    } catch (e: InvalidProtocolBufferException) {
        throw SerializationException("Error deserializing from Protobuf message", e)
    }

    override fun close() {}
}
