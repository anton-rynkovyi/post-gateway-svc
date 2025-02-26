package com.airdodge.post.gateway;

import com.airdodge.post.gateway.config.KafkaTopicConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({KafkaTopicConfiguration.class})
public class PostGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(PostGatewayApplication.class, args);
    }
}