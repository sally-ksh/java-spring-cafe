package com.kakao.cafe.qna.infra;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.kakao.cafe.common.exception.DomainNotFoundException;
import com.kakao.cafe.qna.domain.Article;
import com.kakao.cafe.qna.domain.ArticleRepository;

@Repository
public class MemoryArticleRepository implements ArticleRepository {
	private static final String ERROR_OF_ARTICLE_ID = "article id";

	private Logger logger = LoggerFactory.getLogger(MemoryArticleRepository.class);
	private Map<Long, Article> data = new LinkedHashMap<>();

	@Override
	public void save(Article entity) {
		if (entity.getId() != null && data.containsKey(entity.getId())) {
			data.replace(entity.getId(),
				new Article(entity.getId(),
					entity.getWriter(),
					entity.getTitle(),
					entity.getContent(),
					entity.getWritingDate()));
			return;
		}
		Long id = getNextId();
		data.put(id, entity);
		entity.setId(id);
		logger.info("question db save : {}",entity);
	}

	private Long getNextId() {
		return data.size()+1L;
	}

	@Override
	public Optional<Article> findById(Long id) {
		if (id < 1) {
			throw new IllegalArgumentException(ERROR_OF_ARTICLE_ID);
		}
		if (!data.containsKey(id)) {
			throw new DomainNotFoundException(ERROR_OF_ARTICLE_ID);
		}
		Article article = data.get(id);
		return Optional.of(article);
	}

	@Override
	public void deleteAll() {
		this.data = new LinkedHashMap<>();
	}
}
