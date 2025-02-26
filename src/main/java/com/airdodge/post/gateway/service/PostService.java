package com.airdodge.post.gateway.service;

import com.airdodge.post.gateway.model.MastodonPost;
import reactor.core.publisher.Flux;

public interface PostService {

    Flux<MastodonPost> streamPosts();
}
