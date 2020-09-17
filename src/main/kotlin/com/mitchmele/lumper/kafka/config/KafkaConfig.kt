package com.mitchmele.lumper.kafka.config

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.integration.IntegrationMessageHeaderAccessor
import org.springframework.integration.channel.DirectChannel
import org.springframework.integration.config.EnableIntegration
import org.springframework.integration.core.MessagingTemplate
import org.springframework.integration.kafka.dsl.Kafka
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.ProducerFactory
import org.springframework.kafka.support.DefaultKafkaHeaderMapper
import java.util.HashMap

@Configuration
@EnableIntegration
@ComponentScan
class KafkaConfig {

    @Value(value = "\${spring.kafka.bootstrap-servers}")
    private lateinit var bootstrapAddress: String

    @Bean
    fun errorQueue(): DirectChannel {
        return DirectChannel()
    }

    @Bean
    fun messagingTemplate(): MessagingTemplate {
        return MessagingTemplate()
    }

    @Bean
    fun mapper(): DefaultKafkaHeaderMapper {
        return DefaultKafkaHeaderMapper()
    }

    @Bean
    fun producerFactory(): ProducerFactory<String, String> {
        val configProps = HashMap<String, Any>().apply {
            put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress)
            put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
            put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
            put(ProducerConfig.LINGER_MS_CONFIG, 1)

        }
        return DefaultKafkaProducerFactory(configProps)
    }

    private fun kafkaMessageHandler(producerFactory: ProducerFactory<String, String>, topic: String) =
            Kafka.outboundChannelAdapter(producerFactory)
                    .messageKey<Any> { m -> m.headers[IntegrationMessageHeaderAccessor.SEQUENCE_NUMBER].toString() }
                    .headerMapper(mapper())
                    .sync(true)
                    .partitionId<Any> { _ -> 0 }
                    .topicExpression("headers[kafka_topic] ?: '$topic'")
                    .configureKafkaTemplate { t -> t.id("kafkaTemplate:$topic") }
                    .sendFailureChannel(errorQueue())

}