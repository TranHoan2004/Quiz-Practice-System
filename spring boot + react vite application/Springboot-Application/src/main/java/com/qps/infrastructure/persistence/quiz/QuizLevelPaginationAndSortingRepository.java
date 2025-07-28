package com.qps.infrastructure.persistence.quiz;

import com.qps.domain.quiz.model.QuizLevel;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuizLevelPaginationAndSortingRepository extends PagingAndSortingRepository<QuizLevel, String> {
}
