package com.airdodge.post.gateway.service.impl;

import com.airdodge.post.gateway.model.MastodonPost;
import com.airdodge.post.gateway.service.PostService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverRecord;

@Slf4j
@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    private final KafkaReceiver<String, MastodonPost> kafkaReceiver;

    private final Sinks.Many<MastodonPost> sink = Sinks.many().multicast().onBackpressureBuffer();

    @PostConstruct
    public void init() {
        kafkaReceiver.receive()
                .doOnNext(record -> {
                    record.receiverOffset().acknowledge();
                    log.info("Received post: {}", record.value());
                })
                .map(ReceiverRecord::value)
                .subscribe(
                        post -> {
                            Sinks.EmitResult result = sink.tryEmitNext(post);
                            if (result.isFailure()) {
                                log.warn("Failed to emit post to sink: {}", result);
                            }
                        },
                        error -> log.error("Error in Kafka receiver: ", error)
                );

    }

    @Override
    public Flux<MastodonPost> streamPosts() {
        return sink.asFlux();
    }
}
