package com.airdodge.post.gateway.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties("airdodge.mastodon-connector-svc.kafka.topic")
public class KafkaTopicConfiguration {

    private PostTopic posts;

    @Getter
    @Setter
    public static class PostTopic {
        private String name;
    }
}
