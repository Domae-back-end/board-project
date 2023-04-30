package com.fastcampus.projectboard.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@ToString
@Table(indexes = )
public class Article {

    private Long id;
    private String title;
    private String content;
    private String hashtag;

    private LocalDateTime createAt;
    private String createdBy;
    private LocalDateTime modifiedAt;
    private String modifiedBy;

}
