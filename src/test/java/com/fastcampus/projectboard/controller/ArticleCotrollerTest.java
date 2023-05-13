package com.fastcampus.projectboard.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@DisplayName("View 컨트롤러 - 게시글")
@WebMvcTest
class ArticleCotrollerTest {

    private final MockMvc mvc;

    public ArticleCotrollerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @Test
    @DisplayName("[view] [GET] 게시글 리스트 페이지 - 정상 호출")
    public void givenNoting_whenRequestingArticlesView_thenReturnsArticlesView() throws Exception {
        //Given

        //When&Then
        mvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index.html"))
                .andExpect(model().attributeExists("articles"));

    }

    @Test
    @DisplayName("[view] [GET] 게시글 단일 페이지 - 정상 호출")
    public void givenNoting_whenRequestingArticleView_thenReturnsArticleView() throws Exception {
        //Given

        //When&Then
        mvc.perform(get("/articles/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/detail"))
                .andExpect(model().attributeExists("articleComments"))
                .andExpect(model().attributeExists("article"));

    }

    @Test
    @DisplayName("[view] [GET] 게시글 검색 전용 페이지 - 정상 호출")
    public void givenNoting_whenRequestingArticleSearchView_thenReturnsArticleSearchView() throws Exception {
        //Given

        //When&Then
        mvc.perform(get("/articles/search"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("articles/search"));
    }

    @Test
    @DisplayName("[view] [GET] 게시글 해시태그 검색 전용 페이지 - 정상 호출")
    public void givenNoting_whenRequestingArticleHashtagSearchView_thenReturnsArticleHashtagSearchView() throws Exception {
        //Given

        //When&Then
        mvc.perform(get("/articles/search-hashtag"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(model().attributeExists("articles/search-hashtag"));
    }


}