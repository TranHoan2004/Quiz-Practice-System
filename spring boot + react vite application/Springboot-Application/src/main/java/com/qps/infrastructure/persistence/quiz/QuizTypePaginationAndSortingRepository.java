package com.qps.infrastructure.persistence.quiz;

import com.qps.domain.quiz.model.QuizType;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizTypePaginationAndSortingRepository extends PagingAndSortingRepository<QuizType, String> {
}
