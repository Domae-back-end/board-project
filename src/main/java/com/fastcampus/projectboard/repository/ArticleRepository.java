package com.fastcampus.projectboard.repository;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.QArticle;
import com.fastcampus.projectboard.service.querydsl.ArticleRepositoryCustom;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.StringExpression;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ArticleRepository extends
        JpaRepository<Article, Long>,
        ArticleRepositoryCustom,
        QuerydslPredicateExecutor<Article>,
        QuerydslBinderCustomizer<QArticle>
{

    Page<Article> findByTitle(String title, Pageable pageable);

    Page<Article> findByTitleContaining(String title, Pageable pageable);
    Page<Article> findByContentContaining(String content, Pageable pageable);
    Page<Article> findByUserAccount_UserIdContaining(String userId, Pageable pageable);
    Page<Article> findByUserAccount_NicknameContaining(String nickname, Pageable pageable);
    Page<Article> findByHashtag(String hashtag, Pageable pageable);

    @Override
    default void customize(QuerydslBindings bindings, QArticle root) {
        bindings.excludeUnlistedProperties(true); //모든겁색 금지
        bindings.including(root.title,root.content,root.hashtag,root.createdAt,root.createdBy);
        //이것들로만 검색하게.

        //bindings.bind(root.title).first(StringExpression::likeIgnoreCase); //like ''
        bindings.bind(root.title).first(StringExpression::containsIgnoreCase); //like '%%'
        //대소문자 상관없이 검색.
        bindings.bind(root.content).first(StringExpression::containsIgnoreCase); //like '%%'
        bindings.bind(root.hashtag).first(StringExpression::containsIgnoreCase); //like '%%'
        bindings.bind(root.createdAt).first(DateTimeExpression::eq); //like '%%'
        bindings.bind(root.createdBy).first(StringExpression::containsIgnoreCase); //like '%%'
    }


}