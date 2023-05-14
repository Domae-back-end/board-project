package com.fastcampus.projectboard.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public record ArticleDTO(LocalDateTime createdAt, String createdBy, String title,
                         String content, String hashtag){

    public static ArticleDTO of(LocalDateTime createdAt, String createdBy, String title, String content, String hashtag) {
        return new ArticleDTO(createdAt,createdBy,title,content,hashtag);
    }
}
