package com.fastcampus.projectboard.service;

import com.fastcampus.projectboard.domain.Article;
import com.fastcampus.projectboard.domain.type.SearchType;
import com.fastcampus.projectboard.dto.ArticleDTO;
import com.fastcampus.projectboard.dto.ArticleUpdateDTO;
import com.fastcampus.projectboard.dto.ArticleWithCommentsDTO;
import com.fastcampus.projectboard.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    @Transactional(readOnly = true)
    public Page<ArticleDTO> searchArticles(SearchType searchType, String searchKeyword, Pageable pageable) {

        if (searchKeyword == null || searchKeyword.isBlank()){
            return articleRepository.findAll(pageable).map(ArticleDTO::from);
        }

        return switch (searchType){
            case TITLE -> articleRepository.findByTitleContaining(searchKeyword, pageable).map(ArticleDTO::from);
            case ID -> articleRepository.findByUserAccount_UserIdContaining(searchKeyword,pageable).map(ArticleDTO::from);
            case CONTENT -> articleRepository.findByContentContaining(searchKeyword,pageable).map(ArticleDTO::from);
            case HASHTAG -> articleRepository.findByHashtag("#"+searchKeyword,pageable).map(ArticleDTO::from);
            case NICKNAME -> articleRepository.findByUserAccount_NicknameContaining(searchKeyword,pageable).map(ArticleDTO::from);
        };

    }
    @Transactional(readOnly = true)
    public ArticleWithCommentsDTO getArticle(Long articleId) {
        return articleRepository.findById(articleId)
                .map(ArticleWithCommentsDTO::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - articleId : "+articleId));
    }


    public void saveArticle(ArticleDTO dto) {
        articleRepository.save(dto.toEntity());
    }

    public void updateArticle(ArticleDTO dto) {

        try{
            Article article = articleRepository.getReferenceById(dto.id());
            if(dto.title() != null ) { article.setTitle(dto.title()); }
            if(dto.content()!= null) { article.setContent(dto.content()); }
            article.setHashtag(dto.hashtag());
            //Transactional 가 변경된걸 감지해서 쿼리문을 날림. 고로 save() 안해도됨.
        }catch (EntityNotFoundException e){
            log.warn("게시글 업데이트 실패. 게시글을 찾을수 없습니다 - dto: {}", dto);
            //dto : " + dto 보다는 dto : {}",dto 방식으로 쓸것, 메모리에서 더 좋다.
        }

    }

    public void deleteArticle(long articleId) {
        articleRepository.deleteById(articleId);
    }

    @Transactional(readOnly = true)
    public Page<ArticleDTO> searchArticlesViaHashtag(String hashtag, Pageable pageable) {

        if(hashtag == null || hashtag.isBlank()){
            return Page.empty(pageable);
        }

        return articleRepository.findByHashtag(hashtag,pageable).map(ArticleDTO::from);
    }

    public List<String> getHashtags() {
        return articleRepository.findAllDistinctHashtags();
    }
}
