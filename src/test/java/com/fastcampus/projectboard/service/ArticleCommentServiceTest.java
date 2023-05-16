package com.fastcampus.projectboard.service;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.ArticleComment;
import com.fastcampus.projectboard.domain.UserAccount;
import com.fastcampus.projectboard.dto.ArticleCommentDTO;
import com.fastcampus.projectboard.dto.ArticleDTO;
import com.fastcampus.projectboard.dto.UserAccountDTO;
import com.fastcampus.projectboard.repository.ArticleCommentRepository;
import com.fastcampus.projectboard.repository.ArticleRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

@DisplayName("비즈니스 로직 - 댓글")
@ExtendWith(MockitoExtension.class)
class ArticleCommentServiceTest {
    @InjectMocks
    private ArticleCommentService articleCommentService;
    @Mock
    private ArticleCommentRepository articleCommentRepository;
    @Mock
    private ArticleRepository articleRepository;


    @DisplayName("게시글 ID로 조회하면, 해당하는 댓글 리스트를 반환한다.")
    @Test
    void givenArticleId_whenSearchingArticleComments_thenReturnsArticleComments() {
        // Given
        Long articleId = 1L;
        ArticleComment expected = createArticleComment("content");
        given(articleCommentRepository.findByArticle_Id(articleId)).willReturn(List.of(expected));

        // When
        List<ArticleCommentDTO> actual = articleCommentService.searchArticleComment(articleId);

        // Then
        assertThat(actual)
                .hasSize(1)
                .first().hasFieldOrPropertyWithValue("content", expected.getContent());
        then(articleCommentRepository).should().findByArticle_Id(articleId);
    }

    @DisplayName("댓글 정보를 입력하면, 댓글을 저장한다.")
    @Test
    void givenArticleCommentInfo_whenSavingArticleComment_thenSavesArticleComment() {
        // Given
        ArticleCommentDTO dto = createArticleCommentDto("댓글");
        given(articleRepository.getReferenceById(dto.articleId())).willReturn(createArticle());
        given(articleCommentRepository.save(any(ArticleComment.class))).willReturn(null);

        // When
        articleCommentService.saveArticleComment(dto);

        // Then
        then(articleRepository).should().getReferenceById(dto.articleId());
        then(articleCommentRepository).should().save(any(ArticleComment.class));
    }

    @DisplayName("댓글 저장을 시도했는데 맞는 게시글이 없으면, 경고 로그를 찍고 아무것도 안 한다.")
    @Test
    void givenNonexistentArticle_whenSavingArticleComment_thenLogsSituationAndDoesNothing() {
        // Given
        ArticleCommentDTO dto = createArticleCommentDto("댓글");
        given(articleRepository.getReferenceById(dto.articleId())).willThrow(EntityNotFoundException.class);

        // When
        articleCommentService.saveArticleComment(dto);

        // Then
        then(articleRepository).should().getReferenceById(dto.articleId());
        then(articleCommentRepository).shouldHaveNoInteractions();
    }

    @DisplayName("댓글 정보를 입력하면, 댓글을 수정한다.")
    @Test
    void givenArticleCommentInfo_whenUpdatingArticleComment_thenUpdatesArticleComment() {
        // Given
        String oldContent = "content";
        String updatedContent = "댓글";
        ArticleComment articleComment = createArticleComment(oldContent);
        ArticleCommentDTO dto = createArticleCommentDto(updatedContent);
        given(articleCommentRepository.getReferenceById(dto.id())).willReturn(articleComment);

        // When
        articleCommentService.updateArticleComment(dto);

        // Then
        assertThat(articleComment.getContent())
                .isNotEqualTo(oldContent)
                .isEqualTo(updatedContent);
        then(articleCommentRepository).should().getReferenceById(dto.id());
    }

    @DisplayName("없는 댓글 정보를 수정하려고 하면, 경고 로그를 찍고 아무 것도 안 한다.")
    @Test
    void givenNonexistentArticleComment_whenUpdatingArticleComment_thenLogsWarningAndDoesNothing() {
        // Given
        ArticleCommentDTO dto = createArticleCommentDto("댓글");
        given(articleCommentRepository.getReferenceById(dto.id())).willThrow(EntityNotFoundException.class);

        // When
        articleCommentService.updateArticleComment(dto);

        // Then
        then(articleCommentRepository).should().getReferenceById(dto.id());
    }

    @DisplayName("댓글 ID를 입력하면, 댓글을 삭제한다.")
    @Test
    void givenArticleCommentId_whenDeletingArticleComment_thenDeletesArticleComment() {
        // Given
        Long articleCommentId = 1L;
        willDoNothing().given(articleCommentRepository).deleteById(articleCommentId);

        // When
        articleCommentService.deleteArticleComment(articleCommentId);

        // Then
        then(articleCommentRepository).should().deleteById(articleCommentId);
    }



    private ArticleCommentDTO createArticleCommentDto(String content) {
        return ArticleCommentDTO.of(
                1L,
                1L,
                createUserAccountDto(),
                content,
                LocalDateTime.now(),
                "uno",
                LocalDateTime.now(),
                "uno"
        );
    }

    private UserAccountDTO createUserAccountDto() {
        return UserAccountDTO.of(
                1L,
                "uno",
                "password",
                "uno@mail.com",
                "Uno",
                "This is memo",
                LocalDateTime.now(),
                "uno",
                LocalDateTime.now(),
                "uno"
        );
    }

    private ArticleComment createArticleComment(String content) {
        return ArticleComment.of(
                createUserAccount(),
                Article.of(createUserAccount(),
                        "title",
                        "content",
                        "hashtag"),
                content
        );
    }

    private UserAccount createUserAccount() {
        return UserAccount.of(
                "uno",
                "password",
                "uno@email.com",
                "Uno",
                null
        );
    }

    private Article createArticle() {
        return Article.of(
                createUserAccount(),
                "title",
                "content",
                "#java"
        );
    }
}