package com.crud.response;

import com.crud.domain.PostEntity;
import lombok.Builder;
import lombok.Getter;

/**
 * 서비스 정책에 맞는 응답 클래스
 */
@Getter
public class PostResponse {
    private final Long id;
    private final String title;
    private final String content;

    public PostResponse(PostEntity postEntity) {
        this.id = postEntity.getId();
        this.title = postEntity.getTitle();
        this.content = postEntity.getContent();
    }

    @Builder
    public PostResponse(Long id, String title, String content) {
        this.id = id;
        this.title = title.substring(0, Math.min(title.length(), 20));
        this.content = content;
    }
}
