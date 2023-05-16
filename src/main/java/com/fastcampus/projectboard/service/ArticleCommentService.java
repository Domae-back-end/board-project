package com.fastcampus.projectboard.service;

import com.fastcampus.projectboard.dto.ArticleCommentDTO;
import com.fastcampus.projectboard.repository.ArticleCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleCommentService {

    private final ArticleCommentRepository articleCommentRepository;

    @Transactional(readOnly = true)
    public List<ArticleCommentDTO> searchArticleComment(long articleId) {
        return List.of();
    }

    public void saveArticleComment(ArticleCommentDTO of) {
    }

    public void updateArticleComment(ArticleCommentDTO dto) {
    }

    public void deleteArticleComment(Long articleCommentId) {
    }

}
