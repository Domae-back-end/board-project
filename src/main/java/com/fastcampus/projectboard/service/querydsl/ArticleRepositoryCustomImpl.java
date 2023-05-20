package com.fastcampus.projectboard.service.querydsl;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.QArticle;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class ArticleRepositoryCustomImpl extends QuerydslRepositorySupport implements ArticleRepositoryCustom{

    public ArticleRepositoryCustomImpl() {
        super(Article.class);
    }

    @Override
    public List<String> findAllDistinctHashtags() {

        QArticle qArticle = QArticle.article;

        JPQLQuery<String> query = from(qArticle)
                .distinct()
                .select(qArticle.hashtag)
                .where(qArticle.hashtag.isNotNull());

        return query.fetch();
    }

}
