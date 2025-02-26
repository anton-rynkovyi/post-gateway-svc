package com.airdodge.post.gateway.config;

import com.airdodge.post.gateway.model.MastodonPost;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;

import java.util.Collections;
import java.util.Map;

@Configuration
public class KafkaReceiverConfiguration {

    @Bean
    public KafkaReceiver<String, MastodonPost> kafkaReceiver(@Value("${spring.kafka.bootstrap-servers}") String bootstrapServers,
                                                             @Value("${spring.kafka.consumer.group-id}") String groupId,
                                                             KafkaTopicConfiguration topicConfiguration) {
        Map<String, Object> props = Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                ConsumerConfig.GROUP_ID_CONFIG, groupId,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
                JsonDeserializer.TRUSTED_PACKAGES, "*",
                JsonDeserializer.TYPE_MAPPINGS, "com.airdodge.mastodon.connector.model.MastodonPost:com.airdodge.post.gateway.model.MastodonPost"

        );

        ReceiverOptions<String, MastodonPost> receiverOptions = ReceiverOptions
                .<String, MastodonPost>create(props)
                .subscription(Collections.singleton(topicConfiguration.getPosts().getName()));

        return KafkaReceiver.create(receiverOptions);
    }
}