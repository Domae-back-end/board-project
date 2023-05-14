package com.fastcampus.projectboard.service;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.ArticleComment;
import com.fastcampus.projectboard.domain.UserAccount;
import com.fastcampus.projectboard.dto.ArticleCommentDTO;
import com.fastcampus.projectboard.dto.ArticleDTO;
import com.fastcampus.projectboard.repository.ArticleCommentRepository;
import com.fastcampus.projectboard.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("비즈니스 로직 - 댓글")
@ExtendWith(MockitoExtension.class)
class ArticleCommentServiceTest {
    @InjectMocks
    private ArticleCommentService articleCommentService;
    @Mock
    private ArticleCommentRepository articleCommentRepository;
    @Mock
    private ArticleRepository articleRepository;


    @DisplayName("게시글 ID 로 조회하면, 해당하는 댓글 리스트를 반환한다.")
    @Test
    void givenArticleId_whenSerchComments_thenReturnsComments() {

        //given
        Long articleId = 1L;
        ArticleComment expected = createArticleComment("content");
        given(articleCommentRepository.findByArticle_Id(articleId)).willReturn(List.of(expected));
        //when
        List<ArticleCommentDTO> articleCommentDTOList = articleCommentService.searchArticleComment(articleId);
        //then
        assertThat(articleCommentDTOList)
                .hasSize(1)
                .first().hasFieldOrPropertyWithValue("content", expected.getContent());
        then(articleCommentRepository).should().findByArticle_Id(articleId);
    }


    private ArticleComment createArticleComment(String content) {
        ArticleComment articleComment = ArticleComment.of(
                createUserAccount(),
                createArticle(),
                content
        );

        return articleComment;
    }
    private Article createArticle() {
        Article article = Article.of(
                createUserAccount(),
                "title",
                "content",
                "#java"
        );
        ReflectionTestUtils.setField(article, "id", 1L);

        return article;
    }

    private UserAccount createUserAccount() {
        return UserAccount.of(
                "kwon",
                "1234",
                "ty_ty123@naver.com",
                "kwon",
                null
        );
    }
}