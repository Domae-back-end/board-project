package com.fastcampus.projectboard.controller;

import com.fastcampus.projectboard.config.SecurityConfig;
import com.fastcampus.projectboard.domain.type.SearchType;
import com.fastcampus.projectboard.dto.ArticleWithCommentsDTO;
import com.fastcampus.projectboard.dto.UserAccountDTO;
import com.fastcampus.projectboard.service.ArticleService;
import com.fastcampus.projectboard.service.PaginationService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@DisplayName("View 컨트롤러 - 게시글")
@Import(SecurityConfig.class)
@WebMvcTest(ArticleCotroller.class)
class ArticleCotrollerTest {

    private final MockMvc mvc;

    @MockBean
    private ArticleService articleService;
    @MockBean
    private PaginationService paginationService;

    public ArticleCotrollerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }


    @Test
    @DisplayName("[view] [GET] 게시글 리스트 (게시판 페이지) - 검색어와 정상호출")
    public void givenKeyword_whenSearchingArticlesView_thenReturnsArticlesView() throws Exception {
        //Given
        SearchType searchType = SearchType.TITLE;
        String searchValue = "title";
        given(articleService.searchArticles(eq(searchType), eq(searchValue), any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumnbers(anyInt(), anyInt())).willReturn(List.of(0, 1, 2, 3, 4));
        //When&Then
        mvc.perform(get("/articles")
                        .queryParam("searchType",searchType.name())
                        .queryParam("searchValue",searchValue))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attributeExists("searchTypes"));
        then(articleService).should().searchArticles(eq(searchType), eq(searchValue), any(Pageable.class));
        then(paginationService).should().getPaginationBarNumnbers(anyInt(), anyInt());

    }

    @DisplayName("[view][GET] 게시글 리스트 (게시판) 페이지 - 페이징, 정렬 기능")
    @Test
    void givenPagingAndSortingParams_whenSearchingArticlesPage_thenReturnsArticlesPage() throws Exception {
        // Given
        String sortName = "title";
        String direction = "desc";
        int pageNumber = 0;
        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Order.desc(sortName)));
        List<Integer> barNumbers = List.of(1, 2, 3, 4, 5);
        given(articleService.searchArticles(null, null, pageable)).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumnbers(pageable.getPageNumber(), Page.empty().getTotalPages())).willReturn(barNumbers);

        // When & Then
        mvc.perform(get("/articles")
                        .queryParam("page", String.valueOf(pageNumber))
                        .queryParam("size", String.valueOf(pageSize))
                        .queryParam("sort", sortName + "," + direction))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attribute("paginationBarNumbers", barNumbers));
        then(articleService).should().searchArticles(null, null, pageable);
        then(paginationService).should().getPaginationBarNumnbers(
                pageable.getPageNumber(),
                Page.empty().getTotalPages());
    }

    @Test
    @DisplayName("[view] [GET] 게시글 리스트 페이지 - 정상 호출")
    public void givenNoting_whenRequestingArticlesView_thenReturnsArticlesView() throws Exception {
        //Given
        given(articleService.searchArticles(eq(null), eq(null), any(Pageable.class))).willReturn(Page.empty());
        given(paginationService.getPaginationBarNumnbers(anyInt(), anyInt())).willReturn(List.of(0, 1, 2, 3, 4));
        //When&Then
        mvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/index"))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attributeExists("paginationBarNumbers"));
        then(articleService).should().searchArticles(eq(null), eq(null), any(Pageable.class));
        then(paginationService).should().getPaginationBarNumnbers(anyInt(), anyInt());

    }

    @Test
    @DisplayName("[view] [GET] 게시글 단일 페이지 - 정상 호출")
    public void givenNoting_whenRequestingArticleView_thenReturnsArticleView() throws Exception {
        //Given
        Long articleId = 1L;
        given(articleService.getArticle(articleId)).willReturn(createArticleWithCommentsDto());
        //When&Then
        mvc.perform(get("/articles/" + articleId)).andExpect(status().isOk()).andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.TEXT_HTML)).andExpect(view().name("articles/detail")).andExpect(model().attributeExists("articleComments")).andExpect(model().attributeExists("article"));
        then(articleService).should().getArticle(articleId);
    }

    @Test
    @DisplayName("[view] [GET] 게시글 검색 전용 페이지 - 정상 호출")
    public void givenNoting_whenRequestingArticleSearchView_thenReturnsArticleSearchView() throws Exception {
        // Given

        // When & Then
        mvc.perform(get("/articles/search"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/search"));
    }

    @Test
    @DisplayName("[view] [GET] 게시글 해시태그 검색 전용 페이지 - 정상 호출")
    public void givenNoting_whenRequestingArticleHashtagSearchView_thenReturnsArticleHashtagSearchView() throws Exception {
        //Given
        given(articleService.searchArticlesViaHashtag(eq(null), any(Pageable.class)))
                .willReturn(Page.empty());
        //When&Then
        mvc.perform(get("/articles/search-hashtag"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/search-hashtag"))
                .andExpect(model().attribute("articles",Page.empty()))
                .andExpect(model().attributeExists("hashtags"))
                .andExpect(model().attributeExists("paginationBarNumbers"));
        then(articleService).should()
                .searchArticlesViaHashtag(eq(null), any(Pageable.class));
    }



    @DisplayName("[view][GET] 게시글 해시태그 검색 페이지 - 정상 호출, 해시태그 입력")
    @Test
    public void givenHashtag_whenRequestingArticleSearchHashtagView_thenReturnsArticleSearchHashtagView() throws Exception {
        // Given
        String hashtag = "#java";
        List<String> hashtags = List.of("#java", "#spring", "#boot");
        given(articleService.searchArticlesViaHashtag(eq(hashtag), any(Pageable.class))).willReturn(Page.empty());
        // When & Then
        mvc.perform(
                        get("/articles/search-hashtag")
                                .queryParam("searchValue", hashtag)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
                .andExpect(view().name("articles/search-hashtag"))
                .andExpect(model().attribute("articles", Page.empty()))
                .andExpect(model().attributeExists("hashtags"))
                .andExpect(model().attributeExists("paginationBarNumbers"));

        then(articleService).should().searchArticlesViaHashtag(eq(hashtag), any(Pageable.class));
    }


    private ArticleWithCommentsDTO createArticleWithCommentsDto() {
        return ArticleWithCommentsDTO.of(1L, createUserAccountDto(), Set.of(), "title", "content", "#java", LocalDateTime.now(), "uno", LocalDateTime.now(), "uno");
    }

    private UserAccountDTO createUserAccountDto() {
        return UserAccountDTO.of(1L, "kwon", "1234", "kwon@mail.com", "kwon", "memo", LocalDateTime.now(), "kwon", LocalDateTime.now(), "kwon");
    }

}