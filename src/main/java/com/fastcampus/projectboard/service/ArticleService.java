package com.fastcampus.projectboard.service;

import com.fastcampus.projectboard.domain.type.SearchType;
import com.fastcampus.projectboard.dto.ArticleDTO;
import com.fastcampus.projectboard.dto.ArticleUpdateDTO;
import com.fastcampus.projectboard.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public Page<ArticleDTO> searchArticles(SearchType searchType) {
        return Page.empty();
    }

    @Transactional(readOnly = true)
    public ArticleDTO searchArticle(long l) {
        return null;
    }


    public void saveArticle(ArticleDTO dto) {
    }


    public void updateArticle(long articleId, ArticleUpdateDTO dto) {
    }

    public void deleteArticle(long articleId) {
    }
}
